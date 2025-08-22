const API_BASE = "http://localhost:8080/api/posts";

// Simulated admin user (you can change to match your system)
const adminUserId = 3;

async function loadApprovedPosts() {
  try {
    const res = await fetch(API_BASE);
    const posts = await res.json();

    const approvedPosts = posts.filter((post) => post.status === "APPROVED");
    const totalPosts = posts.length;
    const publishedPosts = posts.filter((post) => post.status === "PUBLISHED").length;
    const reviewPosts = posts.filter((post) => post.status === "REVIEW").length;
    const draftPosts = posts.filter((post) => post.status === "DRAFT").length;

    // Update stats
    document.getElementById("total-posts").textContent = totalPosts;
    document.getElementById("published-posts").textContent = publishedPosts;
    document.getElementById("review-posts").textContent = reviewPosts;
    document.getElementById("draft-posts").textContent = draftPosts;

    const container = document.getElementById("dashboard-posts");
    container.innerHTML = "";

    if (approvedPosts.length === 0) {
      container.innerHTML = `
        <div class="text-center py-12">
          <div class="w-16 h-16 bg-gray-100 rounded-full flex items-center justify-center mx-auto mb-4">
            <svg class="w-8 h-8 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z"/>
            </svg>
          </div>
          <h3 class="text-lg font-semibold text-gray-900 mb-2">No Posts Ready for Publishing</h3>
          <p class="text-gray-600">Approved posts will appear here when they're ready to go live.</p>
        </div>
      `;
      return;
    }

    approvedPosts.forEach((post) => {
      const div = document.createElement("div");
      div.className = "bg-white rounded-xl border border-gray-200 shadow-sm hover:shadow-md transition-all duration-200 overflow-hidden";
      
      div.innerHTML = `
        <div class="p-6">
          <div class="flex items-start justify-between mb-4">
            <div class="flex-1">
              <h3 class="text-xl font-semibold text-gray-900 mb-2">${post.title}</h3>
              <div class="flex items-center gap-4 text-sm text-gray-500 mb-3">
                <span class="flex items-center gap-1">
                  <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"/>
                  </svg>
                  Author ID: ${post.authorId}
                </span>
                <span class="flex items-center gap-1">
                  <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"/>
                  </svg>
                  ${formatDate(post.createdAt)}
                </span>
              </div>
            </div>
            <div class="flex items-center gap-2">
              <span class="px-3 py-1 bg-green-100 text-green-800 text-xs font-medium rounded-full">
                Ready to Publish
              </span>
            </div>
          </div>
          
          <div class="bg-gray-50 rounded-lg p-4 mb-6">
            <p class="text-gray-700 leading-relaxed line-clamp-3">${post.content}</p>
          </div>
          
          <div class="flex flex-col sm:flex-row gap-3">
            <button 
              onclick="changeStatus(${post.id}, 'PUBLISHED')"
              class="flex-1 bg-gradient-to-r from-blue-600 to-purple-600 hover:from-blue-700 hover:to-purple-700 text-white px-6 py-3 rounded-lg font-semibold transition-all duration-200 flex items-center justify-center gap-2 group shadow-lg hover:shadow-xl transform hover:scale-105"
            >
              <svg class="w-5 h-5 group-hover:scale-110 transition-transform" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z"/>
              </svg>
              Publish Now
            </button>
            
            <button 
              onclick="changeStatus(${post.id}, 'REVIEW')"
              class="flex-1 bg-gradient-to-r from-yellow-500 to-orange-500 hover:from-yellow-600 hover:to-orange-600 text-white px-6 py-3 rounded-lg font-semibold transition-all duration-200 flex items-center justify-center gap-2 group shadow-lg hover:shadow-xl transform hover:scale-105"
            >
              <svg class="w-5 h-5 group-hover:scale-110 transition-transform" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z"/>
              </svg>
              Send Back to Review
            </button>
          </div>
        </div>
      `;
      
      container.appendChild(div);
    });
  } catch (err) {
    console.error("Error loading approved posts:", err);
    document.getElementById("dashboard-posts").innerHTML = `
      <div class="text-center py-12">
        <div class="w-16 h-16 bg-red-100 rounded-full flex items-center justify-center mx-auto mb-4">
          <svg class="w-8 h-8 text-red-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"/>
          </svg>
        </div>
        <h3 class="text-lg font-semibold text-gray-900 mb-2">Error Loading Posts</h3>
        <p class="text-gray-600">Failed to load posts. Please try again.</p>
      </div>
    `;
  }
}

async function changeStatus(postId, newStatus) {
  try {
    const res = await fetch(
      `${API_BASE}/${postId}/status?status=${newStatus}&userId=${adminUserId}`,
      { method: "PUT" }
    );

    if (res.ok) {
      const message = newStatus === "PUBLISHED" 
        ? "Post published successfully! 🚀" 
        : "Post sent back to review.";
      showNotification(message, 'success');
      loadApprovedPosts();
    } else {
      const errText = await res.text();
      showNotification("Error updating status: " + errText, 'error');
    }
  } catch (err) {
    showNotification("Network error while updating status.", 'error');
    console.error(err);
  }
}

function showNotification(message, type = 'info') {
  // Create notification element
  const notification = document.createElement('div');
  notification.className = `fixed top-4 right-4 z-50 px-6 py-4 rounded-lg shadow-lg transform transition-all duration-300 ${
    type === 'success' ? 'bg-green-500 text-white' : 
    type === 'error' ? 'bg-red-500 text-white' : 
    'bg-blue-500 text-white'
  }`;
  notification.textContent = message;
  
  document.body.appendChild(notification);
  
  // Remove notification after 3 seconds
  setTimeout(() => {
    notification.remove();
  }, 3000);
}

function formatDate(dateString) {
  const date = new Date(dateString);
  return date.toLocaleDateString('en-US', {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  });
}

window.onload = loadApprovedPosts;

const API_BASE = "http://localhost:8080/api/posts";

async function loadReviewPosts() {
  try {
    const res = await fetch(API_BASE);
    const posts = await res.json();
    const reviewPosts = posts.filter((p) => p.status === "REVIEW");

    const container = document.getElementById("review-posts");
    container.innerHTML = "";

    // Update stats
    document.getElementById("pending-count").textContent = reviewPosts.length;

    if (reviewPosts.length === 0) {
      container.innerHTML = `
        <div class="text-center py-12">
          <div class="w-16 h-16 bg-gray-100 rounded-full flex items-center justify-center mx-auto mb-4">
            <svg class="w-8 h-8 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"/>
            </svg>
          </div>
          <h3 class="text-lg font-semibold text-gray-900 mb-2">No Posts for Review</h3>
          <p class="text-gray-600">All posts have been reviewed or are in other stages.</p>
        </div>
      `;
      return;
    }

    reviewPosts.forEach((post) => {
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
              <span class="px-3 py-1 bg-yellow-100 text-yellow-800 text-xs font-medium rounded-full">
                Awaiting Review
              </span>
            </div>
          </div>
          
          <div class="bg-gray-50 rounded-lg p-4 mb-6">
            <p class="text-gray-700 leading-relaxed line-clamp-3">${post.content}</p>
          </div>
          
          <div class="flex flex-col sm:flex-row gap-3">
            <button 
              onclick="changeStatus(${post.id}, 'APPROVED')"
              class="flex-1 bg-gradient-to-r from-green-500 to-emerald-500 hover:from-green-600 hover:to-emerald-600 text-white px-6 py-3 rounded-lg font-semibold transition-all duration-200 flex items-center justify-center gap-2 group shadow-lg hover:shadow-xl transform hover:scale-105"
            >
              <svg class="w-5 h-5 group-hover:scale-110 transition-transform" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"/>
              </svg>
              Approve Post
            </button>
            
            <button 
              onclick="changeStatus(${post.id}, 'DRAFT')"
              class="flex-1 bg-gradient-to-r from-red-500 to-pink-500 hover:from-red-600 hover:to-pink-600 text-white px-6 py-3 rounded-lg font-semibold transition-all duration-200 flex items-center justify-center gap-2 group shadow-lg hover:shadow-xl transform hover:scale-105"
            >
              <svg class="w-5 h-5 group-hover:scale-110 transition-transform" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z"/>
              </svg>
              Revert to Draft
            </button>
          </div>
        </div>
      `;
      
      container.appendChild(div);
    });
  } catch (error) {
    console.error("Error loading review posts:", error);
    document.getElementById("review-posts").innerHTML = `
      <div class="text-center py-12">
        <div class="w-16 h-16 bg-red-100 rounded-full flex items-center justify-center mx-auto mb-4">
          <svg class="w-8 h-8 text-red-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"/>
          </svg>
        </div>
        <h3 class="text-lg font-semibold text-gray-900 mb-2">Error Loading Posts</h3>
        <p class="text-gray-600">Failed to load posts for review. Please try again.</p>
      </div>
    `;
  }
}

async function changeStatus(postId, newStatus) {
  try {
    const reviewerId = 2; // Simulated reviewer ID
    const res = await fetch(
      `${API_BASE}/${postId}/status?status=${newStatus}&userId=${reviewerId}`,
      { method: "PUT" }
    );

    if (res.ok) {
      // Show success message
      const message = newStatus === 'APPROVED' ? 'Post approved successfully!' : 'Post reverted to draft.';
      showNotification(message, 'success');
      
      // Reload posts
      loadReviewPosts();
    } else {
      showNotification('Failed to update status. Please try again.', 'error');
    }
  } catch (error) {
    console.error("Error changing status:", error);
    showNotification('Network error. Please check your connection.', 'error');
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

// Load posts when page loads
window.onload = loadReviewPosts;

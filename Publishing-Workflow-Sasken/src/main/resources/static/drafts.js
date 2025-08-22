const API_BASE = "http://localhost:8080/api/posts";
const authorId = 1; // Simulated author

async function loadDrafts() {
  try {
    const res = await fetch(API_BASE);
    const posts = await res.json();

    const drafts = posts.filter(
      (p) => p.status === "DRAFT" && p.authorId === authorId
    );

    const container = document.getElementById("draft-posts");
    container.innerHTML = "";

    if (drafts.length === 0) {
      container.innerHTML = "<p class='text-gray-500'>No drafts available.</p>";
      return;
    }

    drafts.forEach((post) => {
      const div = document.createElement("div");
      div.className = "bg-white p-5 rounded shadow";

      div.innerHTML = `
        <h2 class="text-xl font-bold">${post.title}</h2>
        <p class="mb-4">${post.content}</p>
        <div class="flex gap-4">
          <button onclick="editDraft(${post.id})" class="bg-yellow-500 text-white px-4 py-2 rounded hover:bg-yellow-600">✏️ Edit</button>
          <button onclick="deleteDraft(${post.id})" class="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600">🗑️ Delete</button>
        </div>
      `;

      container.appendChild(div);
    });
  } catch (err) {
    console.error("Failed to load drafts:", err);
  }
}

function editDraft(postId) {
  window.location.href = `edit.html?id=${postId}`;
}

async function deleteDraft(postId) {
  if (!confirm("Are you sure you want to delete this draft?")) return;

  const res = await fetch(`${API_BASE}/${postId}`, { method: "DELETE" });
  if (res.ok) {
    alert("Draft deleted.");
    loadDrafts();
  } else {
    alert("Failed to delete draft.");
  }
}

window.onload = loadDrafts;

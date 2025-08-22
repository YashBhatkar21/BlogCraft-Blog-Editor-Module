const API_BASE = "http://localhost:8080/api/posts";

async function loadAllPosts() {
  try {
    const res = await fetch(API_BASE);
    const posts = await res.json();

    const container = document.getElementById("posts");
    container.innerHTML = "";

    if (posts.length === 0) {
      container.innerHTML = `<p class="text-gray-500">No blog posts available.</p>`;
      return;
    }

    posts.forEach((post) => {
      const div = document.createElement("div");
      div.className = "bg-white p-5 rounded shadow";

      div.innerHTML = `
        <h2 class="text-xl font-semibold text-blue-800 mb-1">${post.title}</h2>
        <p class="text-gray-700 mb-2">${post.content}</p>
        <p class="text-sm text-gray-500">Status: <strong>${
          post.status
        }</strong></p>
        <p class="text-xs text-gray-400">Created at: ${formatDate(
          post.createdAt
        )}</p>
        <p class="text-xs text-gray-400">Updated at: ${formatDate(
          post.updatedAt
        )}</p>
      `;

      container.appendChild(div);
    });
  } catch (err) {
    console.error("Error loading posts:", err);
    document.getElementById("posts").innerHTML =
      "<p class='text-red-600'>Failed to load posts.</p>";
  }
}

function formatDate(datetime) {
  if (!datetime) return "N/A";
  const date = new Date(datetime);
  return date.toLocaleString("en-IN", {
    dateStyle: "medium",
    timeStyle: "short",
  });
}

window.onload = loadAllPosts;

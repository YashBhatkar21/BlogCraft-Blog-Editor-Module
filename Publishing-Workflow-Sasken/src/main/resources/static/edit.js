const postId = new URLSearchParams(window.location.search).get("id");

async function loadPost() {
  const res = await fetch(`http://localhost:8080/api/posts/${postId}`);
  const post = await res.json();

  document.getElementById("title").value = post.title;
  document.getElementById("content").value = post.content;
  document.getElementById("author").value = post.authorId;
}

async function updateDraft(sendToReview = false) {
  const title = document.getElementById("title").value.trim();
  const content = document.getElementById("content").value.trim();
  const authorId = parseInt(document.getElementById("author").value);

  if (!title || !content) {
    alert("Title and content are required.");
    return;
  }

  // Update the post
  const response = await fetch(`http://localhost:8080/api/posts/${postId}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      id: postId,
      title,
      content,
      authorId,
    }),
  });

  if (!response.ok) {
    alert("Failed to update draft.");
    return;
  }

  // If Send to Review
  if (sendToReview) {
    const statusRes = await fetch(
      `http://localhost:8080/api/posts/${postId}/status?status=REVIEW&userId=${authorId}`,
      {
        method: "PUT",
      }
    );

    if (statusRes.ok) {
      alert("Draft updated and sent for review!");
      window.location.href = "review.html";
    } else {
      alert("Updated but failed to send for review.");
    }
  } else {
    alert("Draft updated successfully!");
    window.location.href = "drafts.html";
  }
}

window.onload = loadPost;

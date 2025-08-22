Publishing-Workflow-Sasken
Blog Editor Module – Team Contribution (Summary)
What We Built
We added a modern Blog Editor to the platform, making it easy for users to write, format, and publish blog posts with images and videos. The editor is simple, intuitive, and supports all the basics you’d expect.

Key Features Added
Rich text editing (formatting, lists, links, etc.)
Direct image and video uploads
Type your name as author (no dropdowns)
Save as draft or send for review, with clear feedback
Author name is always shown on posts
What We Used
TinyMCE for the editor and media uploads
Workflow
Write your post, add images/videos, and enter your name
Save as draft or send for review (you’ll get a confirmation)
All posts and drafts show the author, content, and status
If you want to see the editor in action, check out create.html or edit.html and try uploading an image or video!

Note: Before running the project, set your own MySQL username and password in src/main/resources/application.properties:

spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password

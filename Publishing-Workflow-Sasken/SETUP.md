# BlogCraft Publishing Workflow - Setup Guide

## Prerequisites
- Java 17 or higher
- MySQL 8.0 or higher
- Maven 3.6 or higher

## Database Setup

### 1. Start MySQL Service
Make sure MySQL service is running on your machine.

### 2. Create Database
Run the following SQL commands in MySQL:
```sql
CREATE DATABASE IF NOT EXISTS blogcraft CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Or use the provided script:
```bash
mysql -u root -p < database-setup.sql
```

### 3. Verify Database Connection
The application is configured to connect to:
- **Host**: localhost
- **Port**: 3306
- **Database**: blogcraft
- **Username**: root
- **Password**: the007yash

## Running the Application

### 1. Build the Project
```bash
mvn clean compile
```

### 2. Run the Application
```bash
mvn spring-boot:run
```

### 3. Access the Application
- **Main Application**: http://localhost:8080
- **Simple Editor**: http://localhost:8080/create.html
- **Advanced Editor**: http://localhost:8080/advanced-editor.html
- **Swagger API Docs**: http://localhost:8080/swagger-ui.html
- **Actuator Health**: http://localhost:8080/actuator/health

## Key Features

### Author Management
- **Free Text Input**: Users can now write their name instead of selecting from dropdown
- **Author Name Field**: Added to store actual author names
- **Validation**: Ensures author name is provided

### Blog Post Workflow
- **Draft → Review → Approved → Published**
- **Status Transitions**: Proper validation of status changes
- **Author Tracking**: Full author information stored

### Advanced Editor Features
- **Rich Text Editor**: Professional WYSIWYG editor powered by Quill.js
- **Media Management**: Upload and manage images, videos, and documents
- **SEO Tools**: Meta titles, descriptions, keywords, and reading time
- **Real-time Preview**: Live preview of formatted content
- **Word Count & Reading Time**: Automatic calculation and display

### Media Support
- **Image Uploads**: JPG, PNG, GIF, WebP with automatic thumbnails
- **Video Uploads**: MP4, AVI, MOV, WMV, FLV
- **Document Support**: PDF, DOC, DOCX
- **Media Library**: Organized file management with metadata
- **Alt Text & Captions**: Accessibility and SEO optimization

### Professional Tools
- **Formatting Options**: Headers, lists, blockquotes, code blocks
- **Text Styling**: Bold, italic, underline, strikethrough
- **Color & Background**: Custom text and background colors
- **Alignment & Indentation**: Text positioning and structure
- **Font Management**: Multiple font families and sizes

## Troubleshooting

### Common Issues

#### 1. MySQL Connection Error
- Ensure MySQL service is running
- Verify database 'blogcraft' exists
- Check username/password in application.properties
- Try connecting manually: `mysql -u root -p`

#### 2. Port Already in Use
- Change server.port in application.properties
- Or kill the process using port 8080

#### 3. Database Table Issues
- The application uses `spring.jpa.hibernate.ddl-auto=update`
- Tables are created automatically on first run
- Check MySQL logs for any errors

#### 4. File Upload Issues
- Ensure uploads directory has write permissions
- Check file size limits in application.properties
- Verify supported file types

### Error Logs
Check the application logs for detailed error information:
```bash
mvn spring-boot:run -X
```

## API Endpoints

### Blog Posts
- `POST /api/posts` - Create new blog post
- `GET /api/posts` - Get all posts
- `GET /api/posts/{id}` - Get specific post
- `PUT /api/posts/{id}` - Update post
- `PUT /api/posts/{id}/status` - Change post status
- `DELETE /api/posts/{id}` - Delete post

### Media Management
- `POST /api/media/upload` - Upload media file
- `GET /api/media/post/{blogPostId}` - Get media by blog post
- `GET /api/media/recent/images` - Get recent images
- `GET /api/media/recent/videos` - Get recent videos
- `GET /api/media/file/{fileName}` - Download media file
- `GET /api/media/thumbnail/{fileName}` - Get thumbnail
- `PUT /api/media/{mediaId}/metadata` - Update media metadata
- `DELETE /api/media/{mediaId}` - Delete media file

## Frontend Features

### Simple Editor (create.html)
- **Basic Blog Creation**: Simple form-based post creation
- **Author Name Input**: Free text author name entry
- **Draft & Review**: Save as draft or submit for review

### Advanced Editor (advanced-editor.html)
- **Professional Interface**: Sidebar tools and main editor area
- **Rich Text Editing**: Full-featured content editor
- **Media Integration**: Drag & drop file uploads
- **SEO Optimization**: Built-in SEO tools
- **Real-time Stats**: Word count, reading time, character count
- **Preview Mode**: Live content preview
- **Responsive Design**: Works on all devices

### Dashboard & Management
- **Post Management**: View, edit, and manage all posts
- **Status Tracking**: Monitor post workflow progress
- **Media Library**: Organized file management
- **User Roles**: Author, Reviewer, Admin permissions

## File Structure

```
src/main/resources/static/
├── index.html              # Main landing page
├── create.html             # Simple blog editor
├── advanced-editor.html    # Professional blog editor
├── drafts.html             # Draft management
├── posts.html              # All posts view
├── dashboard.html          # Analytics dashboard
├── style.css               # Global styles
└── script.js               # Common JavaScript functions
```

## Configuration

### File Upload Settings
```properties
# File Upload Configuration
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=50MB
spring.servlet.multipart.enabled=true

# Media Upload Configuration
app.upload.dir=uploads
app.upload.max-size=10485760
```

### Supported File Types
- **Images**: JPEG, PNG, GIF, WebP
- **Videos**: MP4, AVI, MOV, WMV, FLV
- **Documents**: PDF, DOC, DOCX

## Performance Tips

1. **Image Optimization**: Use WebP format for better compression
2. **Video Compression**: Compress videos before upload
3. **Thumbnail Generation**: Automatic thumbnail creation for images
4. **File Organization**: Organized upload directory structure
5. **Database Indexing**: Proper indexing on frequently queried fields

## Security Features

- **File Type Validation**: Strict file type checking
- **Size Limits**: Configurable file size restrictions
- **Path Traversal Protection**: Secure file handling
- **User Authentication**: Role-based access control
- **Input Validation**: XSS and injection protection

## Getting Started

1. **Setup Database**: Create MySQL database
2. **Run Application**: Start Spring Boot application
3. **Access Editor**: Navigate to advanced-editor.html
4. **Create Post**: Write content with rich text tools
5. **Upload Media**: Add images and videos
6. **Publish**: Submit for review and publication

## Support

For technical support or feature requests:
- Check the troubleshooting section above
- Review application logs for errors
- Verify database connectivity
- Test file upload permissions

---

**BlogCraft Pro** - Professional blog publishing platform for content creators and teams.

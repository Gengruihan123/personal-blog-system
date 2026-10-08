-- Local demo database. Run this as a MySQL administrator.
-- The schema and demo content are created by Spring Boot on first startup.
CREATE DATABASE IF NOT EXISTS personal_blog
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS 'blog_user'@'localhost' IDENTIFIED BY 'blog_password';
GRANT ALL PRIVILEGES ON personal_blog.* TO 'blog_user'@'localhost';
-- If blog_user already exists, this script does not change its password.

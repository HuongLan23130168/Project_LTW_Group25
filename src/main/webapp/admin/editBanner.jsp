<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8" />
    <title>Sửa Banner</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/style.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/banners.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>
    <%@ include file="common/admin_sidebar.jspf" %>
    <%@ include file="common/admin_header.jspf" %>

    <main class="main-content">
        <div class="page-header">
            <h1>Sửa Banner #${banner.id}</h1>
        </div>

        <div class="form-card">
            <form action="${pageContext.request.contextPath}/admin/banners" method="post" enctype="multipart/form-data">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="id" value="${banner.id}">

                <div class="form-grid">
                    <div class="form-group">
                        <label for="title">Tiêu đề</label>
                        <input type="text" id="title" name="title" value="${banner.title}" required>
                    </div>
                    <div class="form-group">
                        <label for="image_file">Chọn ảnh mới (để trống nếu không muốn thay đổi)</label>
                        <input type="file" id="image_file" name="image_file" accept="image/*">
                        <p>Ảnh hiện tại: <img src="${pageContext.request.contextPath}/${banner.image_url}" class="banner-thumbnail-small"></p>
                        <input type="hidden" name="existing_image_url" value="${banner.image_url}">
                    </div>
                    <div class="form-group">
                        <label for="link">Đường dẫn (Link)</label>
                        <input type="text" id="link" name="link" value="${banner.link}">
                    </div>
                    <div class="form-row">
                        <div class="form-group">
                            <label for="display_order">Thứ tự hiển thị</label>
                            <input type="number" id="display_order" name="display_order" value="${banner.display_order}">
                        </div>
                        <div class="form-group">
                            <label for="is_active">Trạng thái</label>
                            <select id="is_active" name="is_active">
                                <option value="true" ${banner.is_active ? 'selected' : ''}>Hiển thị</option>
                                <option value="false" ${not banner.is_active ? 'selected' : ''}>Ẩn</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group form-group-full">
                        <label for="description">Mô tả</label>
                        <textarea id="description" name="description" rows="2">${banner.description}</textarea>
                    </div>
                </div>
                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">Lưu thay đổi</button>
                    <a href="${pageContext.request.contextPath}/admin/banners" class="btn btn-secondary">Hủy</a>
                </div>
            </form>
        </div>
    </main>
</body>
</html>

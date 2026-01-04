<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8" />
    <title>Quản lý Banner</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/style.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/banners.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>
    <%@ include file="common/admin_sidebar.jspf" %>
    <%@ include file="common/admin_header.jspf" %>

    <main class="main-content">
        <div class="banner-header">
            <h1>Quản lý Banner</h1>
        </div>

        <!-- Add Banner Form -->
        <div class="form-card">
            <h2>Thêm Banner Mới</h2>
            <form action="${pageContext.request.contextPath}/admin/banners" method="post" enctype="multipart/form-data" class="add-banner-form">
                <input type="hidden" name="action" value="add">
                <div class="form-grid">
                    <div class="form-group">
                        <label for="title">Tiêu đề</label>
                        <input type="text" id="title" name="title" required>
                    </div>
                    <div class="form-group">
                        <label for="image_file">Chọn hình ảnh</label>
                        <input type="file" id="image_file" name="image_file" required accept="image/*">
                    </div>
                    <div class="form-group">
                        <label for="link">Đường dẫn (Link)</label>
                        <input type="text" id="link" name="link">
                    </div>
                    <div class="form-row">
                        <div class="form-group">
                            <label for="display_order">Thứ tự hiển thị</label>
                            <input type="number" id="display_order" name="display_order" value="0">
                        </div>
                        <div class="form-group">
                            <label for="is_active">Trạng thái</label>
                            <select id="is_active" name="is_active">
                                <option value="true" selected>Hiển thị</option>
                                <option value="false">Ẩn</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group form-group-full">
                        <label for="description">Mô tả</label>
                        <textarea id="description" name="description" rows="2"></textarea>
                    </div>
                </div>
                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">Lưu Banner</button>
                </div>
            </form>
        </div>

        <!-- Banner List -->
        <div class="table-container">
            <table class="data-table">
                <thead>
                    <tr>
                        <th>Thứ tự</th>
                        <th>Hình ảnh</th>
                        <th>Tiêu đề</th>
                        <th>Mô tả</th>
                        <th>Trạng thái</th>
                        <th>Ngày tạo</th>
                        <th>Hành động</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${bannerList}" var="banner">
                        <tr>
                            <td>${banner.display_order}</td>
                            <td><img src="${pageContext.request.contextPath}/${banner.image_url}" alt="Banner Image" class="banner-thumbnail"></td>
                            <td>${banner.title}</td>
                            <td>${banner.description}</td>
                            <td>
                                <c:if test="${banner.is_active}">
                                    <span class="status-badge active">Hiển thị</span>
                                </c:if>
                                <c:if test="${not banner.is_active}">
                                    <span class="status-badge inactive">Ẩn</span>
                                </c:if>
                            </td>
                            <td>
                                <fmt:formatDate value="${banner.created_at}" pattern="dd/MM/yyyy HH:mm" />
                            </td>
                            <td class="actions">
                                <%-- Sửa lại link cho nút Sửa --%>
                                <a href="${pageContext.request.contextPath}/admin/banners?action=edit&id=${banner.id}" class="btn btn-sm btn-edit"><i class="fas fa-edit"></i> Sửa</a>

                                <form action="${pageContext.request.contextPath}/admin/banners" method="post" style="display:inline;">
                                    <input type="hidden" name="action" value="delete">
                                    <input type="hidden" name="id" value="${banner.id}">
                                    <button type="submit" class="btn btn-sm btn-delete" onclick="return confirm('Bạn có chắc chắn muốn xóa banner này không?');">
                                        <i class="fas fa-trash"></i> Xóa
                                    </button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </main>
</body>
</html>

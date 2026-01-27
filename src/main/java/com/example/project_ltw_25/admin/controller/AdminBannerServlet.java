package com.example.project_ltw_25.admin.controller;

import com.example.project_ltw_25.admin.dao.BannerDAO;
import com.example.project_ltw_25.admin.model.Banner;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@WebServlet(name = "BannerServlet", value = "/admin/banners")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10,      // 10MB
        maxRequestSize = 1024 * 1024 * 50    // 50MB
)
public class AdminBannerServlet extends HttpServlet {
    private static final String UPLOAD_DIR = "uploads";
    private final BannerDAO bannerDAO = new BannerDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("edit".equals(action)) {
            showEditForm(req, resp);
        } else {
            showBannerList(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");

        if ("add".equals(action)) {
            handleAddBanner(req, resp);
        } else if ("delete".equals(action)) {
            handleDeleteBanner(req, resp);
        } else if ("update".equals(action)) {
            handleUpdateBanner(req, resp);
        } else if ("restore".equals(action)) { // <--- Thêm cái này
            int id = Integer.parseInt(req.getParameter("id"));
            bannerDAO.restoreBanner(id);
            resp.sendRedirect(req.getContextPath() + "/admin/banners");
        }
    }

    private void showBannerList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Banner> bannerList = bannerDAO.getAllBanners();
        req.setAttribute("bannerList", bannerList);
        req.getRequestDispatcher("/admin/banners.jsp").forward(req, resp);
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            Banner banner = bannerDAO.getBannerById(id);
            req.setAttribute("banner", banner);
            req.getRequestDispatcher("/admin/editBanner.jsp").forward(req, resp);
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/admin/banners");
        }
    }

    private void handleAddBanner(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uploadFilePath = getUploadPath(req);

        // Lấy thông tin banner chính
        String title = req.getParameter("title");
        String description = req.getParameter("description");
        String link = req.getParameter("link");
        int displayOrder = Integer.parseInt(req.getParameter("display_order"));
        boolean isActive = "true".equals(req.getParameter("is_active"));

        // Lấy thông tin banner phụ (mới)
        String subTitle = req.getParameter("sub_title");
        String subDescription = req.getParameter("sub_description");

        // Lưu ảnh chính
        Part mainFilePart = req.getPart("image_file");
        String dbImageUrl = saveFile(mainFilePart, uploadFilePath);

        // Lưu ảnh phụ (mới)
        Part subFilePart = req.getPart("sub_image_file");
        String dbSubImageUrl = saveFile(subFilePart, uploadFilePath);

        Banner newBanner = new Banner();
        newBanner.setTitle(title);
        newBanner.setDescription(description);
        newBanner.setImage_url(dbImageUrl);
        newBanner.setLink(link);
        newBanner.setDisplay_order(displayOrder);
        newBanner.setIs_active(isActive);

        // Gán dữ liệu phụ
        newBanner.setSub_image_url(dbSubImageUrl);
        newBanner.setSub_title(subTitle);
        newBanner.setSub_description(subDescription);

        bannerDAO.addBanner(newBanner);
        resp.sendRedirect(req.getContextPath() + "/admin/banners");
    }

    private void handleUpdateBanner(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        String title = req.getParameter("title");
        String description = req.getParameter("description");
        String link = req.getParameter("link");
        int displayOrder = Integer.parseInt(req.getParameter("display_order"));
        boolean isActive = "true".equals(req.getParameter("is_active"));

        // Thông tin phụ
        String subTitle = req.getParameter("sub_title");
        String subDescription = req.getParameter("sub_description");

        // Xử lý ảnh chính
        String dbImageUrl = req.getParameter("existing_image_url");
        Part mainFilePart = req.getPart("image_file");
        if (mainFilePart != null && mainFilePart.getSize() > 0) {
            dbImageUrl = saveFile(mainFilePart, getUploadPath(req));
        }

        // Xử lý ảnh phụ
        String dbSubImageUrl = req.getParameter("existing_sub_image_url");
        Part subFilePart = req.getPart("sub_image_file");
        if (subFilePart != null && subFilePart.getSize() > 0) {
            dbSubImageUrl = saveFile(subFilePart, getUploadPath(req));
        }

        Banner banner = new Banner();
        banner.setId(id);
        banner.setTitle(title);
        banner.setDescription(description);
        banner.setImage_url(dbImageUrl);
        banner.setLink(link);
        banner.setDisplay_order(displayOrder);
        banner.setIs_active(isActive);

        banner.setSub_image_url(dbSubImageUrl);
        banner.setSub_title(subTitle);
        banner.setSub_description(subDescription);

        bannerDAO.updateBanner(banner);
        resp.sendRedirect(req.getContextPath() + "/admin/banners");
    }

    private void handleDeleteBanner(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            bannerDAO.deleteBanner(id);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        resp.sendRedirect(req.getContextPath() + "/admin/banners");
    }


    private String getUploadPath(HttpServletRequest req) {
        String applicationPath = req.getServletContext().getRealPath("");
        String uploadFilePath = applicationPath + File.separator + UPLOAD_DIR + File.separator + "banners";
        File uploadDir = new File(uploadFilePath);
        if (!uploadDir.exists()) uploadDir.mkdirs();
        return uploadFilePath;
    }

    private String saveFile(Part filePart, String uploadPath) throws IOException {
        if (filePart == null || filePart.getSubmittedFileName() == null || filePart.getSubmittedFileName().isEmpty() || filePart.getSize() <= 0) {
            return "";
        }
        String fileName = System.currentTimeMillis() + "_" + Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        filePart.write(uploadPath + File.separator + fileName);
        return UPLOAD_DIR + "/banners/" + fileName;
    }
}
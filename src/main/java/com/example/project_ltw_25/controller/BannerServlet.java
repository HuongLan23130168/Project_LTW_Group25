package com.example.project_ltw_25.controller;

import com.example.project_ltw_25.dao.BannerDAO;
import com.example.project_ltw_25.model.Banner;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@WebServlet("/admin/banners")
@MultipartConfig
public class BannerServlet extends HttpServlet {

    private static final String UPLOAD_DIR = "uploads";

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        String action = req.getParameter("action");
        if ("edit".equals(action)) {
            showEditForm(req, resp);
        } else {
            showBannerList(req, resp);
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        req.setCharacterEncoding("UTF-8"); // Đảm bảo đọc đúng tiếng Việt
        String action = req.getParameter("action");

        if ("add".equals(action)) {
            handleAddBanner(req, resp);
        } else if ("delete".equals(action)) {
            handleDeleteBanner(req, resp);
        } else if ("update".equals(action)) {
            handleUpdateBanner(req, resp);
        } else {
            resp.sendRedirect(req.getContextPath() + "/admin/banners");
        }
    }

    private void showBannerList(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        BannerDAO bannerDAO = new BannerDAO();
        List<Banner> bannerList = bannerDAO.getAllBanners();
        req.setAttribute("bannerList", bannerList);
        req.getRequestDispatcher("/admin/banners.jsp").forward(req, resp);
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        BannerDAO bannerDAO = new BannerDAO();
        Banner banner = bannerDAO.getBannerById(id);
        req.setAttribute("banner", banner);
        req.getRequestDispatcher("/admin/editBanner.jsp").forward(req, resp);
    }

    private void handleAddBanner(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        String applicationPath = req.getServletContext().getRealPath("");
        String uploadFilePath = applicationPath + File.separator + UPLOAD_DIR + File.separator + "banners";

        File uploadDir = new File(uploadFilePath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String title = req.getParameter("title");
        String description = req.getParameter("description");
        String link = req.getParameter("link");
        int displayOrder = Integer.parseInt(req.getParameter("display_order"));
        boolean isActive = Boolean.parseBoolean(req.getParameter("is_active"));
        
        Part filePart = req.getPart("image_file");
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        String dbImageUrl = "";
        if (fileName != null && !fileName.isEmpty()) {
            filePart.write(uploadFilePath + File.separator + fileName);
            dbImageUrl = UPLOAD_DIR + "/banners/" + fileName;
        }

        Banner newBanner = new Banner();
        newBanner.setTitle(title);
        newBanner.setDescription(description);
        newBanner.setImage_url(dbImageUrl);
        newBanner.setLink(link);
        newBanner.setDisplay_order(displayOrder);
        newBanner.setIs_active(isActive);

        BannerDAO bannerDAO = new BannerDAO();
        bannerDAO.addBanner(newBanner);

        resp.sendRedirect(req.getContextPath() + "/admin/banners");
    }

    private void handleUpdateBanner(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        int id = Integer.parseInt(req.getParameter("id"));
        String title = req.getParameter("title");
        String description = req.getParameter("description");
        String link = req.getParameter("link");
        int displayOrder = Integer.parseInt(req.getParameter("display_order"));
        boolean isActive = Boolean.parseBoolean(req.getParameter("is_active"));
        
        String dbImageUrl = req.getParameter("existing_image_url");
        Part filePart = req.getPart("image_file");
        
        if (filePart != null && filePart.getSize() > 0) {
            String applicationPath = req.getServletContext().getRealPath("");
            String uploadFilePath = applicationPath + File.separator + UPLOAD_DIR + File.separator + "banners";
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            filePart.write(uploadFilePath + File.separator + fileName);
            dbImageUrl = UPLOAD_DIR + "/banners/" + fileName;
        }

        Banner banner = new Banner();
        banner.setId(id);
        banner.setTitle(title);
        banner.setDescription(description);
        banner.setImage_url(dbImageUrl);
        banner.setLink(link);
        banner.setDisplay_order(displayOrder);
        banner.setIs_active(isActive);

        BannerDAO bannerDAO = new BannerDAO();
        bannerDAO.updateBanner(banner);

        resp.sendRedirect(req.getContextPath() + "/admin/banners");
    }

    private void handleDeleteBanner(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            BannerDAO bannerDAO = new BannerDAO();
            bannerDAO.deleteBanner(id);
        } catch (NumberFormatException e) {
            // Xử lý nếu ID không phải là số
            e.printStackTrace();
        }
        
        resp.sendRedirect(req.getContextPath() + "/admin/banners");
    }
}

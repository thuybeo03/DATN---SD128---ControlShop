package com.example.demo.shipperController;

import com.example.demo.model.*;
import com.example.demo.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/order")
public class HomeOrder {

    @Autowired
    private HoaDonService hoaDonService;

    @Autowired
    private HttpSession session;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private GiaoHangService giaoHangService;

    @Autowired
    private ViTriDonHangServices viTriDonHangServices;

    @Autowired
    private GiayChiTietService giayChiTietService;

    @Autowired
    private LSThanhToanService lsThanhToanService;

    @RequestMapping(value = {"", "/", "/home"})
    private String getHomeShipping(Model model){

        showData(model);

        showDataTab1(model);
        return "transportation/index";
    }

    @GetMapping("/delivery/update/{idHD}")
    private String viewUpdateGiaoHang(Model model, @PathVariable UUID idHD){

        showData(model);

        HoaDon hoaDon = hoaDonService.getOne(idHD);
        GiaoHang giaoHang = hoaDon.getGiaoHang();
        List<ViTriDonHang> giaoHangList = viTriDonHangServices.findByGiaoHang(giaoHang);
        int soLanHuy = 0;
        for (ViTriDonHang xx : giaoHangList) {
            if (xx.getTrangThai() == 2){
                soLanHuy ++;
            }
        }

        showDataGH(model, hoaDon);
        showDataTab2(model);
        model.addAttribute("soLanHuy", soLanHuy);
        model.addAttribute("giaoHangList", giaoHangList);
        return "transportation/index";

    }

    @PostMapping("/delivery/update/{idHD}")
    private String updateGiaoHang(Model model, @PathVariable UUID idHD) {

        HoaDon hoaDon = hoaDonService.getOne(idHD);

        showData(model);

        String trangThaiGiaoHang = request.getParameter("trangThaiGiaoHang");

        String thanhPho = request.getParameter("city");
        String district = request.getParameter("district");
        String ward = request.getParameter("ward");
        String moTa = request.getParameter("moTa");

        GiaoHang giaoHang = hoaDon.getGiaoHang();
        List<ViTriDonHang> giaoHangList = viTriDonHangServices.findByGiaoHang(giaoHang);

        int soLanHuy = 0;

        for (ViTriDonHang xx : giaoHangList) {
            if (xx.getTrangThai() == 2){
                soLanHuy ++;
            }
        }

        if (trangThaiGiaoHang.equals("daGuiHang")){

            String donViVanChuyen = request.getParameter("donViVanChuyen");

            giaoHang.setTenDVVC("Human Express");
            giaoHangService.saveGiaoHang(giaoHang);

            if(!donViVanChuyen.equals("humanExpress")){
                String maVanDonGH = request.getParameter("maVanDonGH");
                Double phiGiaoHangGH = Double.parseDouble(request.getParameter("phiGiaoHangGH"));
                if(donViVanChuyen.equals("ghn")){
                    giaoHang.setTenDVVC("Giao hàng nhanh");
                    giaoHang.setPhiGiaoHang(phiGiaoHangGH);
                    giaoHang.setMaVanDon(maVanDonGH);
                }
                if(donViVanChuyen.equals("ghtk")){
                    giaoHang.setTenDVVC("Giao Hàng Tiết Kiệm");
                    giaoHang.setPhiGiaoHang(phiGiaoHangGH);
                    giaoHang.setMaVanDon(maVanDonGH);
                }
                if(donViVanChuyen.equals("viettelPost")){
                    giaoHang.setTenDVVC("Viettel Post");
                    giaoHang.setPhiGiaoHang(phiGiaoHangGH);
                    giaoHang.setMaVanDon(maVanDonGH);
                }
                if(donViVanChuyen.equals("ahamove")){
                    giaoHang.setTenDVVC("Ahamove");
                    giaoHang.setPhiGiaoHang(phiGiaoHangGH);
                    giaoHang.setMaVanDon(maVanDonGH);
                }
                if(donViVanChuyen.equals("grab")){
                    giaoHang.setTenDVVC("Grab");
                    giaoHang.setPhiGiaoHang(phiGiaoHangGH);
                    giaoHang.setMaVanDon(maVanDonGH);
                }
                if(donViVanChuyen.equals("be")){
                    giaoHang.setTenDVVC("Be");
                    giaoHang.setPhiGiaoHang(phiGiaoHangGH);
                    giaoHang.setMaVanDon(maVanDonGH);
                }
                giaoHangService.saveGiaoHang(giaoHang);
            }
            ViTriDonHang viTriDonHang = new ViTriDonHang();

            viTriDonHang.setViTri("Đã lấy/gửi hành thành công ");
            viTriDonHang.setTrangThai(1);
            viTriDonHang.setGiaoHang(hoaDon.getGiaoHang());
            viTriDonHang.setThoiGian(new Date());
            viTriDonHang.setNoiDung(moTa);

            viTriDonHangServices.addViTriDonHang(viTriDonHang);

            showData(model);
            showDataTab2(model);
            return "transportation/index";
        }else if (trangThaiGiaoHang.equals("thanhCong")){
            String viTri = "Đơn hàng đã giao hàng thành công";

            Date date = new Date();
            ViTriDonHang viTriDonHang = new ViTriDonHang();

            viTriDonHang.setViTri(viTri);
            viTriDonHang.setThoiGian(date);
            viTriDonHang.setTrangThai(1);
            viTriDonHang.setNoiDung(moTa);
            viTriDonHang.setGiaoHang(hoaDon.getGiaoHang());
            viTriDonHangServices.addViTriDonHang(viTriDonHang);

            hoaDon.setTrangThai(3);
            giaoHang.setTgThanhToan(date);
            giaoHang.setTgNhan(date);
            hoaDonService.add(hoaDon);

            LichSuThanhToan lichSuThanhToan = new LichSuThanhToan();
            lichSuThanhToan.setHoaDon(hoaDon);
            lichSuThanhToan.setTgThanhToan(date);
            lichSuThanhToan.setSoTienThanhToan(0.0);
            lichSuThanhToan.setKhachHang(hoaDon.getKhachHang());
            lichSuThanhToan.setMaLSTT("LSST0" + date.getTime());
            lichSuThanhToan.setNoiDungThanhToan("Khách hàng đã thanh toán cho đơn hàng");
            lichSuThanhToan.setTrangThai(1);
            lsThanhToanService.addLSTT(lichSuThanhToan);


            showData(model);
            showDataTab2(model);
            return "transportation/index";
        } else if (trangThaiGiaoHang.equals("thatBai")){

            String viTri = "Đơn hàng giao thất bại ( " +moTa + " )";
            ViTriDonHang viTriDonHang = new ViTriDonHang();

            viTriDonHang.setViTri(viTri);
            viTriDonHang.setThoiGian(new Date());
            viTriDonHang.setTrangThai(2);
            viTriDonHang.setNoiDung(moTa);
            viTriDonHang.setGiaoHang(giaoHang);
            viTriDonHangServices.addViTriDonHang(viTriDonHang);

            if (soLanHuy == 2){
                hoaDon.setTrangThai(4);
                giaoHang.setTgHuy(new Date());
                giaoHang.setLyDoHuy(moTa);
                giaoHang.setTrangThai(2);
                hoaDonService.add(hoaDon);

                Date date = new Date();

                ViTriDonHang viTriDonHang2 = new ViTriDonHang();
                viTriDonHang.setViTri("Đơn hàng đã bị hủy");
                viTriDonHang.setThoiGian(new Date());
                viTriDonHang.setTrangThai(2);
                viTriDonHang.setNoiDung(moTa);
                viTriDonHang.setGiaoHang(giaoHang);
                viTriDonHangServices.addViTriDonHang(viTriDonHang2);

                LichSuThanhToan lichSuThanhToan = new LichSuThanhToan();
                lichSuThanhToan.setHoaDon(hoaDon);
                lichSuThanhToan.setTgThanhToan(date);
                lichSuThanhToan.setSoTienThanhToan(hoaDon.getTongTien());
                lichSuThanhToan.setKhachHang(hoaDon.getKhachHang());
                lichSuThanhToan.setMaLSTT("LSST0" + date.getTime());
                lichSuThanhToan.setNoiDungThanhToan("Khách hàng không nhận hàng");
                lichSuThanhToan.setTrangThai(5);
                lsThanhToanService.addLSTT(lichSuThanhToan);

                showDataTab2(model);
                showData(model);
                return "transportation/index";
            }
            showData(model);
            showDataTab2(model);
            return "transportation/index";
        } else {
            String viTri = "Đơn hàng đã đến"  + ", " + ward + ", " + district + " , " + thanhPho;
            ViTriDonHang viTriDonHang = new ViTriDonHang();

            viTriDonHang.setViTri(viTri);
            viTriDonHang.setThoiGian(new Date());
            viTriDonHang.setTrangThai(1);
            viTriDonHang.setNoiDung(moTa);
            viTriDonHang.setGiaoHang(giaoHang);
            viTriDonHangServices.addViTriDonHang(viTriDonHang);

            for (ViTriDonHang x: giaoHangList) {
                if(x.getTrangThai() == 1){
                    x.setTrangThai(0);
                    viTriDonHangServices.addViTriDonHang(x);
                }
            }
            showData(model);
            showDataTab2(model);
            return "transportation/index";

        }
    }

    @GetMapping("/bill/print/{idHD}")
    private String printBill(Model model, @PathVariable UUID idHD){
        HoaDon hoaDon = hoaDonService.getOne(idHD);
        model.addAttribute("billPrint", hoaDon);
        return "transportation/printBill";
    }

    @GetMapping("/delivery/refund/update/{idHD}")
    private String viewUpdateRefundGiaoHang(Model model, @PathVariable UUID idHD){
        showData(model);
        HoaDon hoaDon = hoaDonService.getOne(idHD);
        showDataGHHH(model, hoaDon);
        showDataTab2(model);

        return "transportation/index";

    }


    @GetMapping("/return/{idHD}")
    private String viewReturnGiaoHang(Model model, @PathVariable UUID idHD){
        HoaDon hoaDon = hoaDonService.getOne(idHD);
        GiaoHang giaoHang = hoaDon.getGiaoHang();

        if (giaoHang.getThoiGianHoan() == null){
            model.addAttribute("chuaBatDau", true);
        }else{
            model.addAttribute("daBatDau", true);
        }
        showData(model);
        showDataTab3(model);
        model.addAttribute("showHuyHoanHang", true);
        model.addAttribute("HoaDonVanChuyen", hoaDon);
        List<ViTriDonHang> giaoHangList = viTriDonHangServices.findByGiaoHang(giaoHang);
        model.addAttribute("giaoHangList", giaoHangList);
        return "transportation/index";

    }

    @PostMapping("/return/update/{idHD}")
    private String updateBillReturn(Model model,  @PathVariable UUID idHD){

        HoaDon hoaDon = hoaDonService.getOne(idHD);
        GiaoHang giaoHang = hoaDon.getGiaoHang();
        String trangThaiHoan = request.getParameter("trangThaiHoanHang");

        if (trangThaiHoan.equals("batDauHoan")){
            String maVanDonHoan = request.getParameter("maVanDonGH");
            Double phiHoanHang = Double.parseDouble(request.getParameter("phiGiaoHangGH"));
            giaoHang.setPhiHoanHang(phiHoanHang);
            giaoHang.setMaVanDonHoan(maVanDonHoan);
            giaoHang.setThoiGianHoan(new Date());
            giaoHangService.saveGiaoHang(giaoHang);

            ViTriDonHang viTriDonHang = new ViTriDonHang();
            viTriDonHang.setViTri("Bắt đầu hoàn hàng về kho");
            viTriDonHang.setGiaoHang(giaoHang);
            viTriDonHang.setTrangThai(3);
            viTriDonHang.setThoiGian(new Date());
            viTriDonHangServices.addViTriDonHang(viTriDonHang);

        }else if(trangThaiHoan.equals("dangGiao")){
            String thanhPho = request.getParameter("city");
            String district = request.getParameter("district");
            String ward = request.getParameter("ward");
            String moTa = request.getParameter("moTa");
            String viTri = "Đơn hàng đã đến"  + ", " + ward + ", " + district + " , " + thanhPho + " || " + moTa;

            ViTriDonHang viTriDonHang = new ViTriDonHang();
            viTriDonHang.setThoiGian(new Date());
            viTriDonHang.setGiaoHang(giaoHang);
            viTriDonHang.setViTri(viTri);
            viTriDonHang.setTrangThai(4);
            viTriDonHang.setNoiDung(moTa);
            viTriDonHangServices.addViTriDonHang(viTriDonHang);

        }else if(trangThaiHoan.equals("thanhCong")){
            String thanhPho = request.getParameter("city");
            String district = request.getParameter("district");
            String ward = request.getParameter("ward");
            String moTa = request.getParameter("moTa");
            String viTri = "Đơn hàng đã về kho "  + ", " + ward + ", " + district + " , " + thanhPho + " || " + moTa;

            ViTriDonHang viTriDonHang = new ViTriDonHang();
            viTriDonHang.setThoiGian(new Date());
            viTriDonHang.setGiaoHang(giaoHang);
            viTriDonHang.setViTri(viTri);
            viTriDonHang.setTrangThai(5);
            viTriDonHang.setNoiDung(moTa);
            viTriDonHangServices.addViTriDonHang(viTriDonHang);
            giaoHang.setTrangThai(4);
            giaoHangService.saveGiaoHang(giaoHang);

            for (HoaDonChiTiet xx: hoaDon.getHoaDonChiTiets()) {
                ChiTietGiay chiTietGiay = xx.getChiTietGiay();
                chiTietGiay.setSoLuong(xx.getSoLuong() + xx.getSoLuong());
                giayChiTietService.save(chiTietGiay);
            }


        }

        showData(model);
        showDataTab3(model);
        return "transportation/index";
    }

    @GetMapping("/bill/refund/print/{idHD}")
    private String printBillRefund(Model model, @PathVariable UUID idHD){
        HoaDon hoaDon = hoaDonService.getOne(idHD);
        model.addAttribute("billPrint", hoaDon);
        return "transportation/printBillRefund";
    }

    private void showData(Model model){
        NhanVien nhanVien = (NhanVien) session.getAttribute("shipperLogged");
        List<HoaDon> allHoaDonList = hoaDonService.listAllHoaDonByNhanVien(nhanVien);
        List<HoaDon> hoaDonDGList = hoaDonService.listHoaDonByNhanVienAndTrangThai(nhanVien, 2);
        List<HoaDon> hoaDonDoneList = hoaDonService.listHoaDonHuyAndThanhCongByNhanVien(nhanVien);
        List<HoaDon> hoaDonListHoan= new ArrayList<>();

        model.addAttribute("allHoaDonList",allHoaDonList);
        model.addAttribute("sumDH", allHoaDonList.size() + hoaDonListHoan.size());
        model.addAttribute("dangGiao", hoaDonDGList.size());
        model.addAttribute("sumDHMuaHang", hoaDonDGList.size());
        model.addAttribute("giaoThanhCong", hoaDonDoneList.size());
        model.addAttribute("hoaDonHoan", hoaDonListHoan.size());
        model.addAttribute("hoaDonDGList",hoaDonDGList);
        model.addAttribute("hoaDonDoneList",hoaDonDoneList);
        model.addAttribute("nameNhanVien",nhanVien.getHoTenNV());
    }

    private void showDataGH(Model model, HoaDon hoaDon){
        GiaoHang giaoHangListActive = hoaDon.getGiaoHang();
        model.addAttribute("showHTGH", "true");
        model.addAttribute("HoaDonVanChuyen", hoaDon);
        model.addAttribute("giaoHangListActive", giaoHangListActive);
    }

    private void showDataGHHH(Model model, HoaDon hoaDon){
        GiaoHang giaoHangListActive = hoaDon.getGiaoHang();
        model.addAttribute("showDonHangHoan", "true");
        model.addAttribute("HoaDonVanChuyen", hoaDon);
        model.addAttribute("giaoHangListActive", giaoHangListActive);

    }

    private void showDataTab1(Model model){
        model.addAttribute("activeAll", "nav-link active");
        model.addAttribute("activeVanChuyen", "nav-link");
        model.addAttribute("activeDone", "nav-link");
        model.addAttribute("tabpane1", "tab-pane show active");
        model.addAttribute("tabpane2", "tab-pane");
        model.addAttribute("tabpane3", "tab-pane");
    }

    private void showDataTab2(Model model){
        model.addAttribute("activeAll", "nav-link");
        model.addAttribute("activeVanChuyen", "nav-link active");
        model.addAttribute("activeDone", "nav-link");
        model.addAttribute("tabpane1", "tab-pane");
        model.addAttribute("tabpane2", "tab-pane show active");
        model.addAttribute("tabpane3", "tab-pane");
    }

    private void showDataTab3(Model model){
        model.addAttribute("activeAll", "nav-link");
        model.addAttribute("activeVanChuyen", "nav-link");
        model.addAttribute("activeDone", "nav-link active");
        model.addAttribute("tabpane1", "tab-pane");
        model.addAttribute("tabpane2", "tab-pane");
        model.addAttribute("tabpane3", "tab-pane show active");
    }
}

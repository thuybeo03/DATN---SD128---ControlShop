package com.example.demo.buyerController;

import com.example.demo.model.*;
import com.example.demo.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
@RequestMapping("/buyer")
public class CheckOutController {

    @Autowired
    private HttpSession session;

    @Autowired
    private HoaDonService hoaDonService;

    @Autowired
    private HoaDonChiTietService hoaDonChiTietService;

    @Autowired
    private GiayChiTietService giayChiTietService;

    @Autowired
    private DiaChiKHService diaChiKHService;

    @Autowired
    private GHCTService ghctService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ShippingFeeService shippingFeeService;

    @Autowired
    private LSThanhToanService lsThanhToanService;

    @Autowired
    private GiaoHangService giaoHangService;

    @Autowired
    private VNPayService vnPayService;

    @PostMapping("/checkout")
    private String checkOutCart(Model model, @RequestParam("selectedProducts") List<UUID> selectedProductIds){

        KhachHang khachHang = (KhachHang) session.getAttribute("KhachHangLogin");
        GioHang gioHang = (GioHang) session.getAttribute("GHLogged") ;

        DiaChiKH diaChiKHDefault = diaChiKHService.findDCKHDefaulByKhachHang(khachHang);
        List<DiaChiKH> diaChiKHList = diaChiKHService.findbyKhachHangAndLoaiAndTrangThai(khachHang, false, 1);

        List<HoaDonChiTiet> listHDCTCheckOut = new ArrayList<>();
        Date date = new Date();
        HoaDon hoaDon = new HoaDon();

        String maHD = "HD_" + khachHang.getMaKH() + "_" + date.getDate() + generateRandomNumbers();

        hoaDon.setKhachHang(khachHang);
        hoaDon.setMaHD(maHD);
        hoaDon.setLoaiHD(0);
        hoaDon.setTgTao(date);
        hoaDon.setTrangThai(6);
        hoaDonService.add(hoaDon);

        GiaoHang giaoHang = new GiaoHang();
        giaoHang.setHoaDon(hoaDon);
        giaoHangService.saveGiaoHang(giaoHang);
        hoaDon.setGiaoHang(giaoHang);
        hoaDonService.add(hoaDon);

        if (diaChiKHDefault != null){
            session.removeAttribute("diaChiGiaoHang");
            session.setAttribute("diaChiGiaoHang", diaChiKHDefault);

            giaoHang.setTenNguoiNhan(diaChiKHDefault.getTenNguoiNhan());
            giaoHang.setMaGiaoHang("");
            giaoHang.setSdtNguoiNhan(diaChiKHDefault.getSdtNguoiNhan());
            giaoHang.setDiaChiNguoiNhan(diaChiKHDefault.getDiaChiChiTiet());

            giaoHangService.saveGiaoHang(giaoHang);
            hoaDonService.add(hoaDon);
        }
        for (UUID x: selectedProductIds) {
            HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
            GioHangChiTiet gioHangChiTiet = ghctService.findByCTGActiveAndKhachHangAndTrangThai(giayChiTietService.getByIdChiTietGiay(x), gioHang);

            hoaDonChiTiet.setHoaDon(hoaDon);
            hoaDonChiTiet.setChiTietGiay(giayChiTietService.getByIdChiTietGiay(x));
            hoaDonChiTiet.setDonGia(gioHangChiTiet.getDonGia());
            hoaDonChiTiet.setSoLuong(gioHangChiTiet.getSoLuong());
            hoaDonChiTiet.setTgThem(new Date());
            hoaDonChiTiet.setTrangThai(1);

            hoaDonChiTietService.add(hoaDonChiTiet);

            listHDCTCheckOut.add(hoaDonChiTiet);
        }

        int sumQuantity = listHDCTCheckOut.stream()
                .mapToInt(HoaDonChiTiet::getSoLuong)
                .sum();

        double total = listHDCTCheckOut.stream()
                .mapToDouble(HoaDonChiTiet::getDonGia)
                .sum();

        if(diaChiKHDefault == null){
            model.addAttribute("addNewAddressNulll", true);
            model.addAttribute("addNewAddressNull", true);
        }else{
            model.addAttribute("diaChiKHDefault", diaChiKHDefault);
            model.addAttribute("addNewAddressNotNull", true);
            model.addAttribute("listAddressKH", diaChiKHList);
        }

        hoaDon.setTongSP(sumQuantity);
        hoaDon.setTongTienSanPham(total);

        hoaDonService.add(hoaDon);

        model.addAttribute("sumQuantity", sumQuantity);
        model.addAttribute("total", total);
        model.addAttribute("listProductCheckOut", listHDCTCheckOut);
        model.addAttribute("toTalOder", total);

        if (diaChiKHDefault != null){
            Double shippingFee = shippingFeeService.calculatorShippingFee(hoaDon, 25000.0);
            hoaDon.setTongTien(total + shippingFee);
            hoaDon.setTienShip(shippingFee);
            hoaDonService.add(hoaDon);

            giaoHang.setDiaChiNguoiNhan(diaChiKHDefault.getDiaChiChiTiet());
            giaoHang.setSdtNguoiNhan(diaChiKHDefault.getSdtNguoiNhan());
            giaoHang.setTenNguoiNhan(diaChiKHDefault.getTenNguoiNhan());
            giaoHangService.saveGiaoHang(giaoHang);

            model.addAttribute("shippingFee", shippingFee);
            model.addAttribute("billPlaceOrder", hoaDon);
            model.addAttribute("toTalOder", total  + shippingFee );
            model.addAttribute("tongTienDaGiamVoucherShip", total + shippingFee);

        }else{
            model.addAttribute("tongTienDaGiamVoucherShip", total);
        }

        session.removeAttribute("hoaDonTaoMoi");

        session.setAttribute("hoaDonTaoMoi", hoaDon);

        showData(model);
        return "online/checkout";
    }

    @PostMapping("/checkout/add/address")
    public String addNewAddressPlaceOrder(Model model,@RequestParam(name = "defaultSelected", defaultValue = "false") boolean defaultSelected){

        KhachHang khachHang = (KhachHang) session.getAttribute("KhachHangLogin");
        HoaDon hoaDon = (HoaDon) session.getAttribute("hoaDonTaoMoi") ;

        List<HoaDonChiTiet> hoaDonChiTietList = hoaDonChiTietService.findByHoaDon(hoaDon);
        List<DiaChiKH> diaChiKHList = diaChiKHService.findbyKhachHangAndLoaiAndTrangThai(khachHang, false, 1);

        Date date = new Date();

        if (defaultSelected){
            for (DiaChiKH xxx: diaChiKHService.getAllDiaChiKH()) {
                xxx.setLoai(false);
                diaChiKHService.save(xxx);
            }
        }

        String nameAddress = request.getParameter("nameAddress");
        String fullName = request.getParameter("fullName");
        String phoneAddress = request.getParameter("phoneAddress");
        String city = request.getParameter("city");
        String district = request.getParameter("district");
        String ward = request.getParameter("ward");
        String description = request.getParameter("description");
        String diaChiChiTiet = description + ", " + ward + ", " + district + ", " + city;

        DiaChiKH diaChiKH = new DiaChiKH();

        diaChiKH.setDiaChiChiTiet(diaChiChiTiet);
        diaChiKH.setMoTa(description);
        diaChiKH.setKhachHang(khachHang);
        diaChiKH.setTrangThai(1);
        diaChiKH.setMaDC( "DC_" + khachHang.getMaKH() + date.getDay() + generateRandomNumbers());
        diaChiKH.setSdtNguoiNhan(phoneAddress);
        diaChiKH.setQuanHuyen(district);
        diaChiKH.setTenDC(nameAddress);
        diaChiKH.setTinhTP(city);
        diaChiKH.setTenNguoiNhan(fullName);
        diaChiKH.setXaPhuong(ward);
        diaChiKH.setTgThem(new Date());
        diaChiKH.setLoai(defaultSelected);

        diaChiKHService.save(diaChiKH);

        hoaDonService.add(hoaDon);

        int sumQuantity = hoaDonChiTietList.stream()
                .mapToInt(HoaDonChiTiet::getSoLuong)
                .sum();

        double total = hoaDonChiTietList.stream()
                .mapToDouble(HoaDonChiTiet::getDonGia)
                .sum();

        Double shippingFee = shippingFeeService.calculatorShippingFee(hoaDon, 25000.0);

        hoaDon.setTongTien(total + shippingFee);
        hoaDonService.add(hoaDon);

        GiaoHang giaoHang = hoaDon.getGiaoHang();

        giaoHang.setDiaChiNguoiNhan(diaChiKH.getDiaChiChiTiet());
        giaoHang.setSdtNguoiNhan(diaChiKH.getSdtNguoiNhan());
        giaoHang.setTenNguoiNhan(diaChiKH.getTenNguoiNhan());
        giaoHangService.saveGiaoHang(giaoHang);


        model.addAttribute("sumQuantity", sumQuantity);
        model.addAttribute("total", total);
        model.addAttribute("diaChiKHDefault", diaChiKH);
        model.addAttribute("listProductCheckOut", hoaDonChiTietList);
        model.addAttribute("listAddressKH", diaChiKHList);
        model.addAttribute("addNewAddressNotNull", true);

        model.addAttribute("shippingFee", shippingFee);

        model.addAttribute("tongTienDaGiamVoucherShip", total + shippingFee );
        model.addAttribute("toTalOder", total  + shippingFee );

        session.removeAttribute("diaChiGiaoHang");
        session.setAttribute("diaChiGiaoHang", diaChiKH);
        showData(model);

        return "online/checkout";
    }

    @PostMapping("/checkout/change/address")
    private String changeAddressCheckOut(Model model){

        KhachHang khachHang = (KhachHang) session.getAttribute("KhachHangLogin");
        HoaDon hoaDon = (HoaDon) session.getAttribute("hoaDonTaoMoi") ;

        List<HoaDonChiTiet> hoaDonChiTietList = hoaDonChiTietService.findByHoaDon(hoaDon);
        List<DiaChiKH> diaChiKHList = diaChiKHService.findbyKhachHangAndLoaiAndTrangThai(khachHang, false, 1);

        UUID idDCKH = UUID.fromString(request.getParameter("idDCKH"));
        DiaChiKH diaChiKHChange = diaChiKHService.findByIdDiaChiKH(idDCKH);

        GiaoHang giaoHang = hoaDon.getGiaoHang();

        giaoHang.setDiaChiNguoiNhan(diaChiKHChange.getDiaChiChiTiet());
        giaoHang.setSdtNguoiNhan(diaChiKHChange.getSdtNguoiNhan());
        giaoHang.setTenNguoiNhan(diaChiKHChange.getTenNguoiNhan());
        giaoHangService.saveGiaoHang(giaoHang);

        int sumQuantity = hoaDonChiTietList.stream()
                .mapToInt(HoaDonChiTiet::getSoLuong)
                .sum();
        double total = hoaDonChiTietList.stream()
                .mapToDouble(HoaDonChiTiet::getDonGia)
                .sum();

        hoaDonService.add(hoaDon);

        Double shippingFee = shippingFeeService.calculatorShippingFee(hoaDon, 25000.0);

        hoaDon.setTongTien(total + shippingFee);
        hoaDonService.add(hoaDon);


//      TODO PASSING DATA BEGIN
        model.addAttribute("sumQuantity", sumQuantity);
        model.addAttribute("total", total);
        model.addAttribute("diaChiKHDefault", diaChiKHChange);
        model.addAttribute("listProductCheckOut", hoaDonChiTietList);
        model.addAttribute("listAddressKH", diaChiKHList);
        model.addAttribute("addNewAddressNotNull", true);
        model.addAttribute("shippingFee", shippingFee);

        model.addAttribute("tongTienDaGiamVoucherShip", total + shippingFee );
        model.addAttribute("toTalOder", total  + shippingFee );
//      TODO PASSING DATA END

        session.removeAttribute("diaChiGiaoHang");
        session.setAttribute("diaChiGiaoHang", diaChiKHChange);

        showData(model);

        return "online/checkout";
    }


    @PostMapping("/checkout/placeoder")
    public String placeOrder(Model model){

        HoaDon hoaDon = (HoaDon) session.getAttribute("hoaDonTaoMoi");
        KhachHang khachHang = (KhachHang) session.getAttribute("KhachHangLogin");

        String hinhThucThanhToan = request.getParameter("hinhThucThanhToan");
        String loiNhan = request.getParameter("loiNhan");

        Double shippingFee = shippingFeeService.calculatorShippingFee(hoaDon, 25000.0);

        hoaDon.setTienShip(shippingFee);

        Date ngayBatDau =  hoaDon.getTgTao();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(ngayBatDau);

        calendar.add(Calendar.DATE, shippingFeeService.tinhNgayNhanDuKien(hoaDon));

        Date ngayKetThuc = calendar.getTime();

        hoaDon.setTienShip(shippingFee);

        List<HoaDonChiTiet> hoaDonChiTietList = hoaDonChiTietService.findByHoaDon(hoaDon);

        for (HoaDonChiTiet xx: hoaDonChiTietList) {
            GioHangChiTiet gioHangChiTiet = ghctService.findByCTSPActive(xx.getChiTietGiay());
            gioHangChiTiet.setTrangThai(0);
            ghctService.addNewGHCT(gioHangChiTiet);

            ChiTietGiay chiTietGiay = xx.getChiTietGiay();
            chiTietGiay.setSoLuong(chiTietGiay.getSoLuong() - xx.getSoLuong());
            giayChiTietService.save(chiTietGiay);
        }

        if (hinhThucThanhToan.equals("QRCodeBanking")){

            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();

            double doubleNumber = hoaDon.getTongTien();
            int total = (int) doubleNumber;
            String vnpayUrl = vnPayService.createOrder(total, "orderInfo", baseUrl);

            hoaDon.setHinhThucThanhToan(1);
            hoaDon.setTrangThai(0);
            hoaDonService.add(hoaDon);

            return "redirect:" + vnpayUrl;

//            return "online/checkout";

//            UserForm(model);
//
//            model.addAttribute("maHD", hoaDon.getMaHD());
//            model.addAttribute("thongTinThanhToan", true);
//
//            model.addAttribute("addNewAddressNull", true);
//            model.addAttribute("addNewAddressNulll", false);
//
//            LichSuThanhToan lichSuThanhToan =  new LichSuThanhToan();
//            lichSuThanhToan.setTgThanhToan(new Date());
//            lichSuThanhToan.setSoTienThanhToan(hoaDon.getTongTien());
//            lichSuThanhToan.setNoiDungThanhToan("Đặt hàng " + hoaDon.getMaHD() + " hình thức thanh toán QRCode Baking");
//            lichSuThanhToan.setKhachHang(khachHang);
//            lichSuThanhToan.setHoaDon(hoaDon);
//            lichSuThanhToan.setMaLSTT("LSTT" + khachHang.getMaKH() + generateRandomNumbers());
//            lichSuThanhToan.setTrangThai(0);
//            lsThanhToanService.addLSTT(lichSuThanhToan);

        }else{
            hoaDon.setHinhThucThanhToan(0);
            hoaDon.setTrangThai(1);
            hoaDonService.add(hoaDon);

            LichSuThanhToan lichSuThanhToan =  new LichSuThanhToan();
            lichSuThanhToan.setTgThanhToan(new Date());
            lichSuThanhToan.setSoTienThanhToan(hoaDon.getTongTien());
            lichSuThanhToan.setNoiDungThanhToan("Đặt hàng " + hoaDon.getMaHD() + " hình thức thanh toán khi nhận hàng");
            lichSuThanhToan.setKhachHang(khachHang);
            lichSuThanhToan.setHoaDon(hoaDon);
            lichSuThanhToan.setMaLSTT("LSTT" + khachHang.getMaKH() + generateRandomNumbers());
            lichSuThanhToan.setTrangThai(0);
            lsThanhToanService.addLSTT(lichSuThanhToan);

            UserForm(model);

            return "redirect:/buyer/purchase";
        }


    }

    private void UserForm(Model model){
        KhachHang khachHang = (KhachHang) session.getAttribute("KhachHangLogin");
        GioHang gioHang = (GioHang) session.getAttribute("GHLogged") ;
        List<GioHangChiTiet> listGHCTActive = ghctService.findByGHActive(gioHang);

        Integer sumProductInCart = listGHCTActive.size();

        model.addAttribute("fullNameLogin", khachHang.getHoTenKH());
        model.addAttribute("sumProductInCart", sumProductInCart);
        session.removeAttribute("hoaDonTaoMoi");
        session.removeAttribute("diaChiGiaoHang");
    }

    public String generateRandomNumbers() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int randomNumber = random.nextInt(10); // Tạo số ngẫu nhiên từ 0 đến 9
            sb.append(randomNumber);
        }
        return sb.toString();
    }

    private void showData(Model model){
        GioHang gioHang = (GioHang) session.getAttribute("GHLogged") ;
        model.addAttribute("sumProductInCart", ghctService.findByGHActive(gioHang).size());

        KhachHang khachHang = (KhachHang) session.getAttribute("KhachHangLogin");
        model.addAttribute("fullNameLogin", khachHang.getHoTenKH());
    }
}

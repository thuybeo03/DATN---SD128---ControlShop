delete from dia_chi
delete from province 

delete from thanh_toan
delete from giao_hang
delete from hoa_don_chi_tiet
delete from hoa_don

alter table thanh_toan 
alter column noi_dung_thanh_toan nvarchar(max)

alter table chat_lieu 
alter column ten_chat_lieu  nvarchar(max)

alter table chuc_vu 
alter column ten_chuc_vu nvarchar(max)

alter table ctgview_model
alter column ten_giay nvarchar(max)
alter table ctgview_model
alter column ten_mau nvarchar(max)

alter table dia_chi 
alter column mien nvarchar(max)
alter table dia_chi 
alter column mo_ta nvarchar(max)
alter table dia_chi 
alter column quan_huyen nvarchar(max)
alter table dia_chi 
alter column ten_dia_chi nvarchar(max)
alter table dia_chi 
alter column tinh_tp nvarchar(max)
alter table dia_chi 
alter column xa_phuong nvarchar(max)
alter table dia_chi 
alter column ten_nguoi_nhan nvarchar(max)
alter table dia_chi 
alter column dia_chi_chi_tiet nvarchar(max)

alter table giao_hang 
alter column noi_dung nvarchar(max)

alter table vi_tri_don_hang
alter column vi_tri_dh nvarchar(max)

alter table giay 
alter column mo_ta nvarchar(max)
alter table giay 
alter column ten_giay nvarchar(max)

alter table giay_view_model
alter column ten_mau nvarchar(max)

alter table hang 
alter column ten_hang nvarchar(max)

alter table thanh_toan 
alter column noi_dung_thanh_toan nvarchar(max)

alter table giao_hang 
alter column dia_chi_nguoi_nhan nvarchar(max)
alter table giao_hang 
alter column ten_nguoi_nhan nvarchar(max)
alter table giao_hang 
alter column ly_do_huy nvarchar(max)
alter table giao_hang 
alter column sdt_nguoi_nhan nvarchar(max)

alter table khach_hang 
alter column ho_ten_kh nvarchar(max)
alter table khach_hang 
alter column dia_chi_kh nvarchar(max)

alter table loai_khach_hang 
alter column ten_loaikh nvarchar(max)

alter table mau_sac 
alter column ten_mau nvarchar(max)

alter table nhan_vien 
alter column dia_chi_nv nvarchar(max)
alter table nhan_vien 
alter column ho_ten_nv nvarchar(max)

alter table province 
alter column province_name nvarchar(max)

alter table vi_tri_don_hang
alter column vi_tri_dh nvarchar(max)


INSERT INTO province (province_id, province_name, transport_coefficient) VALUES

(1, N'Hà Nội', 1),
(2, N'Hà Giang', 6),
(3, N'Cao Bằng',6),
(4, N'Bắc Kạn',5),
(5, N'Tuyên Quang', 4),
(6, N'Lào Cai', 6),
(7, N'Điện Biên', 6),
(8, N'Lai Châu', 6),
(9, N'Sơn La', 4),
(10, N'Yên Bái' ,5),
(11, N'Hoà Bình', 2),
(12, N'Thái Nguyên', 2),
(13, N'Lạng Sơn', 4),
(14, N'Quảng Ninh' , 4),
(15, N'Bắc Giang', 3),
(16, N'Phú Thọ', 2),
(17, N'Vĩnh Phúc', 2),
(18, N'Bắc Ninh', 2),
(19, N'Hải Dương', 2),
(20, N'Hải Phòng', 2),
(21, N'Hưng Yên', 2),
(22, N'Thái Bình', 2),
(23, N'Hà Nam', 2),
(24, N'Nam Định', 2),
(25, N'Ninh Bình',2),
(26, N'Thanh Hóa', 2),
(27, N'Nghệ An', 3),
(28, N'Hà Tĩnh', 3),
(29, N'Quảng Bình', 3),
(30, N'Quảng Trị', 3),
(31, N'Thừa Thiên Huế', 3),
(32, N'Đà Nẵng', 4),
(33, N'Quảng Nam', 4),
(34, N'Quảng Ngãi', 4),
(35, N'Bình Định', 4),
(36, N'Phú Yên', 4),
(37, N'Khánh Hòa', 4),
(38, N'Ninh Thuận', 4),
(39, N'Bình Thuận', 4),
(40, N'Kon Tum', 4),
(41, N'Gia Lai', 4),
(42, N'Đắk Lắk', 4),
(43, N'Đắk Nông', 5),
(44, N'Lâm Đồng', 5),
(45, N'Bình Phước', 5),
(46, N'Tây Ninh', 5),
(47, N'Bình Dương', 5),
(48, N'Đồng Nai', 5),
(49, N'Bà Rịa - Vũng Tàu', 5),
(50, N'Hồ Chí Minh', 4),
(51, N'Long An', 5),
(52, N'Tiền Giang', 5),
(53, N'Bến Tre', 5),
(54, N'Trà Vinh', 5),
(55, N'Vĩnh Long', 5),
(56, N'Đồng Tháp', 6),
(57, N'An Giang', 6),
(58, N'Kiên Giang', 6),
(59, N'Cần Thơ', 6),
(60, N'Hậu Giang', 6),
(61, N'Sóc Trăng', 6),
(62, N'Bạc Liêu', 6),
(63, N'Cà Mau', 6);
-- phpMyAdmin SQL Dump
-- version 4.8.0.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th8 08, 2019 lúc 06:48 PM
-- Phiên bản máy phục vụ: 10.1.32-MariaDB
-- Phiên bản PHP: 7.1.17

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `sims`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `application`
--

CREATE TABLE `application` (
  `id` int(11) NOT NULL,
  `reason` varchar(200) NOT NULL,
  `middle_expect` float DEFAULT NULL,
  `final_expect` float DEFAULT NULL,
  `other_expect` float DEFAULT NULL,
  `mark_expect` float DEFAULT NULL,
  `new_middle` float DEFAULT NULL,
  `new_final` float DEFAULT NULL,
  `new_other` float DEFAULT NULL,
  `new_mark` float DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `remarking_id` int(11) NOT NULL,
  `student_id` int(11) NOT NULL,
  `classes_id` int(11) NOT NULL,
  `subject_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `attendance`
--

CREATE TABLE `attendance` (
  `student_id` int(11) NOT NULL,
  `classes_id` int(11) NOT NULL,
  `subject_id` int(11) NOT NULL,
  `middle_mark` float DEFAULT NULL,
  `final_mark` float DEFAULT NULL,
  `other_mark` float DEFAULT NULL,
  `mark` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Đang đổ dữ liệu cho bảng `attendance`
--

INSERT INTO `attendance` (`student_id`, `classes_id`, `subject_id`, `middle_mark`, `final_mark`, `other_mark`, `mark`) VALUES
(1, 1, 1, NULL, NULL, NULL, NULL),
(1, 1, 2, NULL, NULL, NULL, NULL),
(2, 1, 1, NULL, NULL, NULL, NULL),
(2, 1, 2, NULL, NULL, NULL, NULL),
(3, 1, 1, NULL, NULL, NULL, NULL),
(3, 1, 2, NULL, NULL, NULL, NULL),
(4, 1, 1, NULL, NULL, NULL, NULL),
(4, 1, 2, NULL, NULL, NULL, NULL),
(5, 1, 1, NULL, NULL, NULL, NULL),
(5, 1, 2, NULL, NULL, NULL, NULL),
(5, 2, 3, 1, 2, 3, 2),
(6, 2, 3, NULL, NULL, NULL, NULL),
(6, 2, 4, NULL, NULL, NULL, NULL),
(7, 2, 3, 4, 5, 6, 5),
(7, 2, 4, NULL, NULL, NULL, NULL),
(8, 2, 3, 7, 8, 9, 8.5),
(8, 2, 4, NULL, NULL, NULL, NULL),
(9, 2, 3, 2, 4, 6, 4.5),
(9, 2, 4, NULL, NULL, NULL, NULL),
(10, 2, 3, 8, 10, 2, 9.5),
(10, 2, 4, NULL, NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `calendar`
--

CREATE TABLE `calendar` (
  `classes_id` int(11) NOT NULL,
  `subject_id` int(11) NOT NULL,
  `room` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Đang đổ dữ liệu cho bảng `calendar`
--

INSERT INTO `calendar` (`classes_id`, `subject_id`, `room`) VALUES
(1, 1, 'C32'),
(1, 2, 'C32'),
(2, 3, 'C31'),
(2, 4, 'C31');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `classes`
--

CREATE TABLE `classes` (
  `id` int(11) NOT NULL,
  `name` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Đang đổ dữ liệu cho bảng `classes`
--

INSERT INTO `classes` (`id`, `name`) VALUES
(1, '17HCB'),
(2, '18HCB');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `remarking`
--

CREATE TABLE `remarking` (
  `id` int(11) NOT NULL,
  `opening` date NOT NULL,
  `closing` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `student`
--

CREATE TABLE `student` (
  `id` int(11) NOT NULL,
  `code` varchar(20) NOT NULL,
  `name` varchar(50) NOT NULL,
  `gender` varchar(5) NOT NULL,
  `id_number` varchar(12) DEFAULT NULL,
  `classes_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Đang đổ dữ liệu cho bảng `student`
--

INSERT INTO `student` (`id`, `code`, `name`, `gender`, `id_number`, `classes_id`) VALUES
(1, '1742001', 'Nguyễn Văn A', 'Nam', '123456789', 1),
(2, '1742002', 'Trần Văn B', 'Nam', '234567891', 1),
(3, '1742003', 'Huỳnh Thị C', 'Nữ', '345678912', 1),
(4, '1742004', 'Mai Văn D', 'Nam', '456789123', 1),
(5, '1742005', 'Hồ Thị E', 'Nữ', '567891234', 1),
(6, '1842001', 'Lý Văn F', 'Nam', '678912345', 2),
(7, '1842002', 'Chiêu Văn G', 'Nam', '789123456', 2),
(8, '1842003', 'Trần Thị H', 'Nữ', '891234567', 2),
(9, '1842004', 'Mặc Văn I', 'Nam', '912345678', 2),
(10, '1842005', 'Văn Thị J', 'Nữ', '987654321', 2);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `subject`
--

CREATE TABLE `subject` (
  `id` int(11) NOT NULL,
  `code` varchar(20) NOT NULL,
  `name` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Đang đổ dữ liệu cho bảng `subject`
--

INSERT INTO `subject` (`id`, `code`, `name`) VALUES
(1, 'CTT011', 'Thiết kế giao diện'),
(2, 'CTT012', 'Kiểm chứng phần mềm'),
(3, 'CTT001', 'Lập trình ứng dụng Java'),
(4, 'CTT002', 'Mạng máy tính');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `user`
--

CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(100) NOT NULL,
  `role` varchar(10) NOT NULL,
  `student_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Đang đổ dữ liệu cho bảng `user`
--

INSERT INTO `user` (`id`, `username`, `password`, `role`, `student_id`) VALUES
(2, 'giaovu', '9c5e8ed003d1ccebd3674e7040f844d6', 'ADMIN', NULL),
(3, '1742001', '667ea33f9bc0aab473c62191a356b914', 'USER', 1),
(4, '1742002', 'bb4d9ea54bd79ea436fd5b5f0f5f3cc0', 'USER', 2),
(5, '1742003', 'd9e491c09f0746599411e0004b704519', 'USER', 3),
(6, '1742004', 'f01a8bbed6833c89d3adb2491e40f516', 'USER', 4),
(7, '1742005', 'ca4ea3a264d302a35889e44155bbf251', 'USER', 5),
(8, '1842001', '8238a029b380d41fa7eb2245eb546bbc', 'USER', 6),
(9, '1842002', '9210b2289e0fdaeddcd322a0378fbf17', 'USER', 7),
(10, '1842003', '64e5ebfa6735fc0eafd609c1b455fe6d', 'USER', 8),
(11, '1842004', 'a0962d9d747cddd0f4386624a5d82bf5', 'USER', 9),
(12, '1842005', '5acd51b09f2f087d3bd8fa811368b00c', 'USER', 10);

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `application`
--
ALTER TABLE `application`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_application_remarking1` (`remarking_id`),
  ADD KEY `fk_application_attendance1` (`student_id`,`classes_id`,`subject_id`);

--
-- Chỉ mục cho bảng `attendance`
--
ALTER TABLE `attendance`
  ADD PRIMARY KEY (`student_id`,`classes_id`,`subject_id`),
  ADD KEY `fk_attendance_calendar1` (`classes_id`,`subject_id`);

--
-- Chỉ mục cho bảng `calendar`
--
ALTER TABLE `calendar`
  ADD PRIMARY KEY (`classes_id`,`subject_id`),
  ADD KEY `fk_calendar_subject1` (`subject_id`);

--
-- Chỉ mục cho bảng `classes`
--
ALTER TABLE `classes`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `remarking`
--
ALTER TABLE `remarking`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `student`
--
ALTER TABLE `student`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_student_classes1` (`classes_id`);

--
-- Chỉ mục cho bảng `subject`
--
ALTER TABLE `subject`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_user_student1` (`student_id`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `application`
--
ALTER TABLE `application`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `classes`
--
ALTER TABLE `classes`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT cho bảng `remarking`
--
ALTER TABLE `remarking`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `student`
--
ALTER TABLE `student`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

--
-- AUTO_INCREMENT cho bảng `subject`
--
ALTER TABLE `subject`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT cho bảng `user`
--
ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `application`
--
ALTER TABLE `application`
  ADD CONSTRAINT `fk_application_attendance1` FOREIGN KEY (`student_id`,`classes_id`,`subject_id`) REFERENCES `attendance` (`student_id`, `classes_id`, `subject_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_application_remarking1` FOREIGN KEY (`remarking_id`) REFERENCES `remarking` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Các ràng buộc cho bảng `attendance`
--
ALTER TABLE `attendance`
  ADD CONSTRAINT `fk_attendance_calendar1` FOREIGN KEY (`classes_id`,`subject_id`) REFERENCES `calendar` (`classes_id`, `subject_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_attendance_student1` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Các ràng buộc cho bảng `calendar`
--
ALTER TABLE `calendar`
  ADD CONSTRAINT `fk_calendar_classes1` FOREIGN KEY (`classes_id`) REFERENCES `classes` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_calendar_subject1` FOREIGN KEY (`subject_id`) REFERENCES `subject` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Các ràng buộc cho bảng `student`
--
ALTER TABLE `student`
  ADD CONSTRAINT `fk_student_classes1` FOREIGN KEY (`classes_id`) REFERENCES `classes` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Các ràng buộc cho bảng `user`
--
ALTER TABLE `user`
  ADD CONSTRAINT `fk_user_student1` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

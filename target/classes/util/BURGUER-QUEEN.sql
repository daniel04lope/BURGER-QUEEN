-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Versión del servidor:         8.0.33 - MySQL Community Server - GPL
-- SO del servidor:              Win64
-- HeidiSQL Versión:             12.8.0.6908
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Volcando estructura de base de datos para burger-queen
CREATE DATABASE IF NOT EXISTS `burger-queen` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `burger-queen`;

-- Volcando estructura para tabla burger-queen.administradores
CREATE TABLE IF NOT EXISTS `administradores` (
  `id_admin` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `apellido` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `fecha_contratacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `estado` enum('activo','inactivo','suspendido') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT 'activo',
  `telefono` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `direccion` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `fecha_nacimiento` date DEFAULT NULL,
  `ruta` varchar(6000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id_admin`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Volcando datos para la tabla burger-queen.administradores: ~1 rows (aproximadamente)
INSERT INTO `administradores` (`id_admin`, `nombre`, `apellido`, `email`, `username`, `password`, `fecha_contratacion`, `estado`, `telefono`, `direccion`, `fecha_nacimiento`, `ruta`) VALUES
	(38, 'admin', 'admin', 'admin@gmail.com', 'ADMIN', '123456', '2024-11-29 16:34:09', 'activo', '662062693', 'ADMIN', '2024-11-13', 'barbacoaburger.jpg'),
	(48, 'empleado2', 'empleado2', 'empleado@gmail.com', 'empleado', '123456', '2024-12-03 12:01:45', 'activo', '662062693', 'empleado', '2024-12-03', NULL);

-- Volcando estructura para tabla burger-queen.alergeno
CREATE TABLE IF NOT EXISTS `alergeno` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `NOMBRE` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NOMBRE` (`NOMBRE`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Volcando datos para la tabla burger-queen.alergeno: ~6 rows (aproximadamente)
INSERT INTO `alergeno` (`ID`, `NOMBRE`) VALUES
	(3, 'Frutos secos'),
	(1, 'Gluten'),
	(5, 'Huevo'),
	(2, 'Lactosa'),
	(4, 'Mariscos'),
	(6, 'Pescado');

-- Volcando estructura para tabla burger-queen.carrito
CREATE TABLE IF NOT EXISTS `carrito` (
  `id_carrito` int NOT NULL AUTO_INCREMENT,
  `id_cliente` int DEFAULT NULL,
  `fecha_creacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `estado` enum('pendiente','confirmado','cancelado') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT 'pendiente',
  PRIMARY KEY (`id_carrito`),
  KEY `FK_carrito_usuarios` (`id_cliente`),
  CONSTRAINT `FK_carrito_usuarios` FOREIGN KEY (`id_carrito`) REFERENCES `usuarios` (`id_usuario`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Volcando datos para la tabla burger-queen.carrito: ~1 rows (aproximadamente)
INSERT INTO `carrito` (`id_carrito`, `id_cliente`, `fecha_creacion`, `estado`) VALUES
	(22, 22, '2024-12-02 09:10:26', 'pendiente'),
	(23, 23, '2024-12-03 11:44:34', 'pendiente');

-- Volcando estructura para tabla burger-queen.carrito_items
CREATE TABLE IF NOT EXISTS `carrito_items` (
  `id_item` int NOT NULL AUTO_INCREMENT,
  `id_carrito` int NOT NULL,
  `id_plato` int NOT NULL,
  `cantidad` int NOT NULL,
  `precio_unitario` decimal(10,2) NOT NULL,
  `Detalles` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '',
  `estado` enum('Pendiente','Tramitado') COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id_item`),
  KEY `precio_unitario` (`precio_unitario`),
  KEY `carrito_items_ibfk_1` (`id_carrito`),
  KEY `carrito_items_ibfk_2` (`id_plato`),
  CONSTRAINT `carrito_items_ibfk_1` FOREIGN KEY (`id_carrito`) REFERENCES `carrito` (`id_carrito`)
) ENGINE=InnoDB AUTO_INCREMENT=538 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Volcando datos para la tabla burger-queen.carrito_items: ~12 rows (aproximadamente)

-- Volcando estructura para tabla burger-queen.carta
CREATE TABLE IF NOT EXISTS `carta` (
  `id_producto` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `descripcion` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `precio` decimal(10,2) NOT NULL,
  `categoria` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `peso` decimal(5,2) DEFAULT NULL,
  `ruta` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `alergenos` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT 'Ninguno',
  PRIMARY KEY (`id_producto`),
  KEY `precio` (`precio`)
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Volcando datos para la tabla burger-queen.carta: ~17 rows (aproximadamente)
INSERT INTO `carta` (`id_producto`, `nombre`, `descripcion`, `precio`, `categoria`, `peso`, `ruta`, `alergenos`) VALUES
	(33, 'HAMBURGUESA CLASICA', 'Jugosa carne de res a la parrilla, sazonada a la perfección, acompañada de lechuga fresca, tomate, cebolla, pepinillos y queso cheddar derretido. Todo servido en un suave pan brioche tostado. ¡La tradición en cada mordida!', 8.99, 'HAMBURGUESA', 0.25, 'CLASICA.jpg', ''),
	(34, 'HAMBURGUESA DE POLLO', 'Tierna pechuga de pollo empanizada y crujiente, con lechuga fresca, tomate, queso cheddar y mayonesa, servida en un pan brioche tostado. Un clásico lleno de sabor y textura irresistible.', 7.99, 'Burgers', 0.30, 'depollo.jpg', ''),
	(35, 'HAMBURGUESA VEGANA', 'Jugosa hamburguesa vegetal a base de legumbres y especias, acompañada de lechuga crujiente, tomate fresco, cebolla caramelizada y un toque de salsa vegana, servida en un suave pan integral. Sabor auténtico y 100% vegetal.', 8.49, 'Burgers', 0.22, 'vegana.jpg', ''),
	(36, 'HAMBURGUESA DE QUESO', 'Deliciosa hamburguesa de carne jugosa, cubierta con una rebanada de queso fundido que se derrite perfectamente. Acompañada de lechuga fresca, tomate, cebolla y una suave salsa especial, todo servido en un pan recién horneado. Un clásico irresistible para los amantes del queso.', 10.99, 'Burgers', 0.40, 'hamburguesa.png', ''),
	(37, 'HAMBURGUESA DE BACON', 'Una hamburguesa suculenta con carne de res perfectamente cocida, coronada con crujientes tiras de bacon dorado. Complementada con queso fundido, lechuga, tomate y cebolla, y aderezada con una salsa especial que realza todos sus sabores. Servida en un pan recién horneado, esta hamburguesa es la combinación perfecta de sabor y textura para los amantes del bacon.', 9.99, 'Hamburguesa', 0.35, 'bacon.jpg', ''),
	(41, 'HAMBURGUESA ESPECIAL', 'Una opción irresistible para los amantes de los sabores terrosos y delicados. Esta hamburguesa combina carne de res jugosa con champiñones salteados en mantequilla y ajo, acompañada de queso derretido y una pizca de perejil fresco. Todo se sirve en un pan artesanal tostado que realza la textura y sabor de cada bocado. Ideal para disfrutar un plato con un toque gourmet y un sabor profundo y delicioso.', 10.49, 'Burgers', 0.33, 'champ.jpg', ''),
	(42, 'Hamburguesa de Aguacate', 'Disfruta de la Avocado Burger, una deliciosa hamburguesa que combina la jugosidad de una carne de calidad con la suavidad y frescura del aguacate. Servida en un pan suave y tostado, esta hamburguesa incluye ingredientes frescos como lechuga crujiente, tomate y cebolla, realzados con una capa de aguacate cremoso que aporta un sabor único y un toque de suavidad. Perfecta para quienes buscan una opción sabrosa y nutritiva, ideal para disfrutar en cualquier ocasión.', 9.79, 'Burgers', 0.31, 'aguacate.jpg', 'ALERGENOS:'),
	(43, 'NUGGUETS DE POLLO', 'Prueba nuestros Nuggets de Pollo, una opción irresistible para los amantes de los sabores clásicos. Hechos con pechuga de pollo 100% real, empanados y fritos hasta obtener un exterior dorado y crujiente, mientras que el interior se mantiene tierno y jugoso. Perfectos para compartir o disfrutar como un snack, vienen acompañados de tu salsa favorita para sumergir y realzar su sabor. ¡El bocado perfecto para cualquier momento del día!', 2.00, 'complementos', 0.25, 'Nugguets_pollo.png', ''),
	(44, 'COCA-COLA', 'Disfruta de la clásica Coca-Cola, la bebida refrescante que ha conquistado al mundo con su sabor inconfundible. Su combinación perfecta de burbujas, dulzura y un toque de cítricos te ofrece una experiencia única en cada sorbo. Ideal para acompañar tus comidas, momentos de relax o celebraciones, esta icónica soda es sinónimo de refresco y alegría. ¡Siente la chispa y el sabor de la Coca-Cola en cada trago!', 5.00, 'Bebida', 1.00, 'Cocacola.png', ''),
	(45, 'PATATAS FRITAS', 'Las patatas fritas son el acompañamiento perfecto para cualquier comida. Crujientes por fuera y tiernas por dentro, estas deliciosas tiras de papa se doran a la perfección para ofrecer un sabor inigualable. Espolvoreadas con sal o acompañadas de tu salsa favorita, las patatas fritas son un snack irresistible que siempre complace. Ya sea como guarnición o como aperitivo, su textura y sabor las convierten en una elección popular en cualquier ocasión.', 10.00, 'complementos', 1.00, 'Patatas_clasicas.png', ''),
	(46, 'AROS DE CEBOLLA', 'Los aros de cebolla son un snack crujiente y sabroso, ideales para compartir o disfrutar por sí solos. Hechos de cebolla fresca rebozada y frita, ofrecen una textura crujiente por fuera y un corazón tierno y jugoso por dentro. Perfectos para acompañar hamburguesas, sandwiches o como aperitivo, los aros de cebolla se destacan por su sabor ligeramente dulce y su toque salado, a menudo servidos con una variedad de salsas para realzar su sabor.', 2.00, 'complementos', 1.00, 'Aros_cebolla.png', ''),
	(47, 'PATATAS CLASICAS', 'Las patatas clásicas son un acompañamiento irresistible y versátil. Doradas y crujientes por fuera, con un interior suave y esponjoso, estas patatas son el complemento perfecto para cualquier comida. Sazonadas con un toque de sal, realzan el sabor de hamburguesas, sándwiches y platos principales, o se disfrutan solas como un snack sabroso. Ideales para compartir o como un antojo personal, las patatas clásicas siempre son una opción que satisface y nunca pasa de moda.', 2.00, 'complemento', 1.00, 'Patatas_clasicas.png', ''),
	(48, 'PATATAS SUPREME', 'Las patatas Supreme son crujientes patatas fritas cubiertas con queso derretido, bacon crujiente y un toque de salsa especial. Se terminan con crema agria y cebollino fresco, ofreciendo una mezcla de sabores irresistibles. Perfectas como acompañamiento o plato principal, son el deleite ideal para quienes buscan un toque gourmet en un clásico.', 2.00, 'complemento', 1.00, 'Patatas_supreme.png', ''),
	(49, 'FANTA', 'Fanta es una refrescante bebida gaseosa con un sabor afrutado y vibrante. Con su sabor a naranja natural y burbujas efervescentes, es la elección perfecta para refrescarte y disfrutar de un momento de alegría. Ideal para compartir en reuniones, comidas o como un toque de sabor durante el día.', 2.00, 'Bebida', 1.00, 'Fanta_naranja.png', ''),
	(50, 'NESTEA', 'Nestlé Ice Tea es una bebida refrescante de té helado con un sabor ligeramente dulce y afrutado. Perfecta para combatir el calor y revitalizarte, su mezcla de té y notas cítricas la convierte en una opción deliciosa para cualquier momento del día. Ideal para disfrutar solo o acompañando tus comidas favoritas.', 1.00, 'Bebida', 1.00, 'Nestea.png', ''),
	(51, 'COCACOLA ZERO', 'Coca-Cola Zero es la versión sin azúcar de la clásica Coca-Cola, ofreciendo el mismo sabor refrescante y único, pero sin calorías. Perfecta para aquellos que buscan disfrutar del sabor icónico de Coca-Cola sin comprometer su consumo de azúcar. Ideal para acompañar tus comidas o disfrutar en cualquier momento del día, brindando una experiencia de sabor auténtico sin remordimientos.', 1.00, 'Bebida', 1.00, 'Cocacola_zero.png', ''),
	(61, 'producto', 'aresrdtfyguhilj', 2.00, 'proiductio', 2.00, 'barbacoaburger.jpg', '');

-- Volcando estructura para tabla burger-queen.empleados
CREATE TABLE IF NOT EXISTS `empleados` (
  `id_empleado` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `apellido` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `fecha_contratacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `posicion` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `estado` enum('activo','inactivo','suspendido') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT 'activo',
  `telefono` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `direccion` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `fecha_nacimiento` date DEFAULT NULL,
  `lectura` tinyint NOT NULL DEFAULT '0',
  `escritura` tinyint NOT NULL DEFAULT '0',
  `control_total` tinyint NOT NULL DEFAULT '0',
  `ruta` varchar(6000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id_empleado`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Volcando datos para la tabla burger-queen.empleados: ~2 rows (aproximadamente)
INSERT INTO `empleados` (`id_empleado`, `nombre`, `apellido`, `email`, `username`, `password`, `fecha_contratacion`, `posicion`, `estado`, `telefono`, `direccion`, `fecha_nacimiento`, `lectura`, `escritura`, `control_total`, `ruta`) VALUES
	(50, 'test', 'test', 'test@gmail.com', 'test', '123456', '2024-12-03 12:03:23', 'test', 'activo', '662062693', 'test', '2024-12-11', 0, 0, 0, 'imagen_1733227403742.jpg');

-- Volcando estructura para tabla burger-queen.modulos
CREATE TABLE IF NOT EXISTS `modulos` (
  `id_modulo` int NOT NULL AUTO_INCREMENT,
  `nombre_modulo` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id_modulo`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Volcando datos para la tabla burger-queen.modulos: ~2 rows (aproximadamente)
INSERT INTO `modulos` (`id_modulo`, `nombre_modulo`) VALUES
	(1, 'CARTA'),
	(2, 'RESERVAS'),
	(3, 'PEDIDO');

-- Volcando estructura para tabla burger-queen.pedidos
CREATE TABLE IF NOT EXISTS `pedidos` (
  `id_pedido` int NOT NULL AUTO_INCREMENT,
  `id_carrito` int NOT NULL,
  `id_usuario` int NOT NULL,
  `estado` enum('en_curso','finalizado') COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id_pedido`),
  KEY `FK__carrito` (`id_carrito`),
  KEY `FK__usuarios` (`id_usuario`),
  CONSTRAINT `FK__carrito` FOREIGN KEY (`id_carrito`) REFERENCES `carrito` (`id_carrito`) ON UPDATE CASCADE,
  CONSTRAINT `FK__usuarios` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Volcando datos para la tabla burger-queen.pedidos: ~0 rows (aproximadamente)

-- Volcando estructura para tabla burger-queen.permisos
CREATE TABLE IF NOT EXISTS `permisos` (
  `id_permiso` int NOT NULL AUTO_INCREMENT,
  `id_empleado` int NOT NULL,
  `id_modulo` int NOT NULL,
  `lectura` tinyint(1) DEFAULT '0',
  `escritura` tinyint(1) DEFAULT '0',
  `control_total` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id_permiso`),
  KEY `id_empleado` (`id_empleado`),
  KEY `id_modulo` (`id_modulo`),
  CONSTRAINT `permisos_ibfk_1` FOREIGN KEY (`id_empleado`) REFERENCES `empleados` (`id_empleado`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `permisos_ibfk_2` FOREIGN KEY (`id_modulo`) REFERENCES `modulos` (`id_modulo`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=168 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Volcando datos para la tabla burger-queen.permisos: ~6 rows (aproximadamente)
INSERT INTO `permisos` (`id_permiso`, `id_empleado`, `id_modulo`, `lectura`, `escritura`, `control_total`) VALUES
	(165, 50, 1, 0, 1, 0),
	(166, 50, 2, 1, 0, 0),
	(167, 50, 3, 1, 0, 0);

-- Volcando estructura para tabla burger-queen.reservas
CREATE TABLE IF NOT EXISTS `reservas` (
  `id_reserva` int NOT NULL AUTO_INCREMENT,
  `nombre_cliente` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `fecha_reserva` date NOT NULL,
  `hora_reserva` time NOT NULL,
  `numero_personas` int NOT NULL,
  `notas` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `estado` enum('pendiente','confirmada','cancelada') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT 'pendiente',
  `fecha_creacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_actualizacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `mesa` int DEFAULT NULL,
  `telefono` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id_reserva`),
  KEY `FK_reservas_usuarios` (`nombre_cliente`),
  KEY `FK_reservas_usuarios_2` (`telefono`),
  CONSTRAINT `FK_reservas_usuarios_2` FOREIGN KEY (`telefono`) REFERENCES `usuarios` (`telefono`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Volcando datos para la tabla burger-queen.reservas: ~0 rows (aproximadamente)
INSERT INTO `reservas` (`id_reserva`, `nombre_cliente`, `fecha_reserva`, `hora_reserva`, `numero_personas`, `notas`, `estado`, `fecha_creacion`, `fecha_actualizacion`, `mesa`, `telefono`) VALUES
	(30, 'nombre23', '2024-12-17', '10:00:00', 2, 'nose', 'pendiente', '2024-12-03 21:37:08', '2024-12-03 21:40:00', 0, '662062693'),
	(31, 'usuario', '2024-12-03', '10:00:00', 2, 'nose', 'pendiente', '2024-12-03 21:40:43', '2024-12-03 21:40:43', NULL, '662062693'),
	(32, 'usuario5', '2024-12-03', '10:00:00', 6, '', 'pendiente', '2024-12-03 21:40:50', '2024-12-03 21:41:23', 0, '662062693');

-- Volcando estructura para tabla burger-queen.usuarios
CREATE TABLE IF NOT EXISTS `usuarios` (
  `id_usuario` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `apellido` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `fecha_registro` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `estado` enum('activo','inactivo','suspendido') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT 'activo',
  `telefono` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `direccion` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `fecha_nacimiento` date DEFAULT NULL,
  `ruta` varchar(6000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id_usuario`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `nombre` (`nombre`),
  KEY `telefono` (`telefono`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Volcando datos para la tabla burger-queen.usuarios: ~1 rows (aproximadamente)
INSERT INTO `usuarios` (`id_usuario`, `nombre`, `apellido`, `email`, `username`, `password`, `fecha_registro`, `estado`, `telefono`, `direccion`, `fecha_nacimiento`, `ruta`) VALUES
	(22, 'usuario', 'usuario', 'usuario@gmail.com', 'usuario', '1234', '2024-12-02 09:10:25', 'activo', '662062693', 'usuario', '2024-12-19', 'aguacate.jpg'),
	(23, 'presentacion', 'test', 'test@gmail.com', 'test', '1234', '2024-12-03 11:44:33', 'activo', '662062693', 'test', '2024-12-03', 'barbacoaburger.jpg');

-- Volcando estructura para disparador burger-queen.before_carrito_item_insert
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `before_carrito_item_insert` BEFORE INSERT ON `carrito_items` FOR EACH ROW BEGIN
  IF NEW.cantidad NOT IN (0, 1) THEN
    SET NEW.cantidad = 1; -- Default to 1 if the cantidad is not 0 or 1
  END IF;
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- Volcando estructura para disparador burger-queen.before_carrito_item_update
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `before_carrito_item_update` BEFORE UPDATE ON `carrito_items` FOR EACH ROW BEGIN
  IF NEW.cantidad NOT IN (0, 1) THEN
    SET NEW.cantidad = 1; -- Default to 1 if the cantidad is not 0 or 1
  END IF;
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- Volcando estructura para disparador burger-queen.validar_boolean_empleados_insert
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `validar_boolean_empleados_insert` BEFORE INSERT ON `empleados` FOR EACH ROW BEGIN
    IF NEW.lectura NOT IN (0, 1) THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Error: El valor de lectura debe ser 0 o 1';
    END IF;
    IF NEW.escritura NOT IN (0, 1) THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Error: El valor de escritura debe ser 0 o 1';
    END IF;
    IF NEW.control_total NOT IN (0, 1) THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Error: El valor de control_total debe ser 0 o 1';
    END IF;
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- Volcando estructura para disparador burger-queen.validar_boolean_empleados_update
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `validar_boolean_empleados_update` BEFORE UPDATE ON `empleados` FOR EACH ROW BEGIN
    IF NEW.lectura NOT IN (0, 1) THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Error: El valor de lectura debe ser 0 o 1';
    END IF;
    IF NEW.escritura NOT IN (0, 1) THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Error: El valor de escritura debe ser 0 o 1';
    END IF;
    IF NEW.control_total NOT IN (0, 1) THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Error: El valor de control_total debe ser 0 o 1';
    END IF;
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- Volcando estructura para disparador burger-queen.validar_boolean_permisos_insert
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `validar_boolean_permisos_insert` BEFORE INSERT ON `permisos` FOR EACH ROW BEGIN
    IF NEW.lectura NOT IN (0, 1) THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Error: El valor de lectura debe ser 0 o 1';
    END IF;
    IF NEW.escritura NOT IN (0, 1) THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Error: El valor de escritura debe ser 0 o 1';
    END IF;
    IF NEW.control_total NOT IN (0, 1) THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Error: El valor de control_total debe ser 0 o 1';
    END IF;
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- Volcando estructura para disparador burger-queen.validar_boolean_permisos_update
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `validar_boolean_permisos_update` BEFORE UPDATE ON `permisos` FOR EACH ROW BEGIN
    IF NEW.lectura NOT IN (0, 1) THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Error: El valor de lectura debe ser 0 o 1';
    END IF;
    IF NEW.escritura NOT IN (0, 1) THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Error: El valor de escritura debe ser 0 o 1';
    END IF;
    IF NEW.control_total NOT IN (0, 1) THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Error: El valor de control_total debe ser 0 o 1';
    END IF;
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- Volcando estructura para disparador burger-queen.validar_email
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='';
DELIMITER //
CREATE TRIGGER `validar_email` BEFORE INSERT ON `empleados` FOR EACH ROW BEGIN
    -- Comprobamos que el email contenga el formato básico de un correo electrónico
    IF NEW.email NOT REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$' THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Error: El formato del correo electrónico no es válido';
    END IF;
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- Volcando estructura para disparador burger-queen.validar_email_administradores
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='';
DELIMITER //
CREATE TRIGGER `validar_email_administradores` BEFORE INSERT ON `administradores` FOR EACH ROW BEGIN
    -- Comprobamos que el email contenga el formato básico de un correo electrónico
    IF NEW.email NOT REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$' THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Error: El formato del correo electrónico no es válido';
    END IF;
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- Volcando estructura para disparador burger-queen.validar_email_usuarios
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='';
DELIMITER //
CREATE TRIGGER `validar_email_usuarios` BEFORE INSERT ON `usuarios` FOR EACH ROW BEGIN
    -- Comprobamos que el email contenga el formato básico de un correo electrónico
    IF NEW.email NOT REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$' THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Error: El formato del correo electrónico no es válido';
    END IF;
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- Volcando estructura para disparador burger-queen.validar_permisos_empleados
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='';
DELIMITER //
CREATE TRIGGER `validar_permisos_empleados` BEFORE INSERT ON `empleados` FOR EACH ROW BEGIN
    IF NEW.lectura NOT IN (0, 1) THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Error: El valor de lectura debe ser 0 o 1';
    END IF;
    IF NEW.escritura NOT IN (0, 1) THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Error: El valor de escritura debe ser 0 o 1';
    END IF;
    IF NEW.control_total NOT IN (0, 1) THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Error: El valor de control_total debe ser 0 o 1';
    END IF;
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- Volcando estructura para disparador burger-queen.validar_telefono
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='';
DELIMITER //
CREATE TRIGGER `validar_telefono` BEFORE INSERT ON `empleados` FOR EACH ROW BEGIN
    -- Comprobar que el número de teléfono contiene solo dígitos y tiene entre 8 y 15 caracteres
    IF NEW.telefono NOT REGEXP '^[0-9]{8,15}$' THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Error: El número de teléfono debe contener solo dígitos y tener entre 8 y 15 caracteres';
    END IF;
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- Volcando estructura para disparador burger-queen.validar_telefono_administradores
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='';
DELIMITER //
CREATE TRIGGER `validar_telefono_administradores` BEFORE INSERT ON `administradores` FOR EACH ROW BEGIN
    -- Comprobar que el número de teléfono contiene solo dígitos y tiene entre 8 y 15 caracteres
    IF NEW.telefono NOT REGEXP '^[0-9]{8,15}$' THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Error: El número de teléfono debe contener solo dígitos y tener entre 8 y 15 caracteres';
    END IF;
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- Volcando estructura para disparador burger-queen.validar_telefono_usuarios
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='';
DELIMITER //
CREATE TRIGGER `validar_telefono_usuarios` BEFORE INSERT ON `usuarios` FOR EACH ROW BEGIN
    -- Comprobar que el número de teléfono contiene solo dígitos y tiene entre 8 y 15 caracteres
    IF NEW.telefono NOT REGEXP '^[0-9]{8,15}$' THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Error: El número de teléfono debe contener solo dígitos y tener entre 8 y 15 caracteres';
    END IF;
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

drop database if exists DBKinalMartIN5BM;
create database DBKinalMartIN5BM;
use DBKinalMartIN5BM;

set global time_zone = '-6:00';


create table Clientes (
	clienteId int not null,
    NIT varchar(10) not null,
    nombreCliente varchar(50) not null,
    apellidoCliente varchar(50) not null,
    direccionCliente varchar(150) not null,
    telefonoCliente varchar(8) not null,
    correoCliente varchar(45) not null,
    PRIMARY KEY PKclienteId (clienteId) 
);

create table Proveedores (
	proveedorId int not null,
	nitProveedor varchar(10) not null,
    nombreProveedor varchar(60) not null,
    apellidoProveedor varchar(60) not null,
    direccionProveedor varchar(150) not null,
    razonSocial varchar(60) not null,
	contactoPrincipal varchar(100) not null,
    paginaWeb varchar(50) not null,
    PRIMARY KEY PKproveedorId (proveedorId)
);

create table Compras (
	numeroDocumento int not null,
    fechaDocumento date not null,
    descripcion varchar(60) not null,
    totalDocumento decimal (10,2) not null,
    PRIMARY KEY PKnumeroDocumento (numeroDocumento)
);

create table TipoProducto (
	codigoTipoProducto int not null,
    descripcion varchar(45) not null,
    PRIMARY KEY PKcodigoTipoProducto (codigoTipoProducto)
);

create table CargoEmpleado (
	codigoCargoEmpleado int not null,
    nombreCargo varchar(45) not null,
    descripcionCargo varchar(45) not null,
    PRIMARY KEY PKcodigoCargoEmpleado (codigoCargoEmpleado)
);

create table Productos (
	codigoProducto int not null,
    descripcionProducto varchar(45),
	precioUnitario decimal(10,2),
    precioDocena decimal(10,2),
    precioMayor decimal(10,2),
    existencia int not null,
    codigoTipoProducto int not null,
    proveedorId int not null,
    PRIMARY KEY PKcodigoProducto (codigoProducto),
    CONSTRAINT FK_codigoTipoProducto foreign key TipoProducto(codigoTipoProducto)
		references TipoProducto(codigoTipoProducto),
	CONSTRAINT FK_proveedorId foreign key Proveedores(proveedorId)
		references Proveedores(proveedorId) 
);	

create table Empleados (
	empleadoId int not null,
    nombreEmpleado varchar(50) not null,
    apellidoEmpleado varchar(50) not null,
    sueldo decimal (10,2) not null,
    direccion varchar(150) not null,
    turno varchar(15) not null,
    codigoCargoEmpleado int not null,
	PRIMARY KEY PKempleadoId (empleadoId),
    CONSTRAINT FK_codigoCargoEmpleado foreign key CargoEmpleado(codigoCargoEmpleado)
		references CargoEmpleado(codigoCargoEmpleado)
);

create table DetalleCompra (
	codigoDetalleCompra int not null,
    costoUnitario decimal(10,2) not null,
    cantidad int not null,
    codigoProducto int not null,
    numeroDocumento int not null,
    PRIMARY KEY PKcodigoDetalleCompra (codigoDetalleCompra),
    CONSTRAINT FK_codigoProducto foreign key Productos(codigoProducto)
		references Productos(codigoProducto),
	CONSTRAINT FK_numeroDocumento foreign key Compras(numeroDocumento)
		references Compras(numeroDocumento)
);

create table Factura (
	numeroFactura int not null,
    estado varchar(50) not null,
    totalFactura decimal (10,2) not null,
    fechaFactura date not null,
    clienteId int not null,
    PRIMARY KEY PKnumeroFactura (numeroFactura),
    CONSTRAINT FK_clienteId foreign key Clientes(clienteId)
		references Clientes(clienteId)
);

create table DetalleFactura(
	codigoDetalleFactura int not null,
    precioUnitario decimal(10,2),
    cantidad int not null,
    numeroFactura int not null,
    codigoProducto int not null,
    primary key PK_DetalleFactura(codigoDetalleFactura),
    constraint FK_DetalleFactura_Factura foreign key DetalleFactura(numeroFactura)
		references Factura(numeroFactura) on delete cascade,
	constraint FK_DetalleFactura foreign key DetalleFactura(codigoProducto)
		references Productos(codigoProducto) on delete cascade
);
-- AGREGAR
Delimiter $$
	create procedure sp_AgregarClientes (in clienteId int, in NIT varchar(10), in nombreCliente varchar(50), in apellidoCliente varchar(50), in direccionCliente varchar(150), in telefonoCliente varchar(8), in correoCliente varchar(45))
    begin
		insert into Clientes(clienteId, NIT, nombreCliente, apellidoCliente, direccionCliente, telefonoCliente, correoCliente)
			values (clienteId, NIT, nombreCliente, apellidoCliente, direccionCliente, telefonoCliente, correoCliente);
            
	End $$
Delimiter ;

call sp_AgregarClientes(1, 'NITodoFeo', 'Pancho', 'Villa', 'Pollo Campero atrasito', 44443333, 'pvillela-2021656');
call sp_AgregarClientes(2, 'NITodoFeo', 'Pancho', 'Villa', 'Pollo Campero atrasito', 44443333, 'pvillela-2021656');


Delimiter $$
	create procedure sp_AgregarProveedores (in proveedorId int, in nitProveedor varchar(10), in nombreProveedor varchar(60), in apellidoProveedor varchar(60), in direccionProveedor varchar(150), in razonSocial varchar(60), in contactoPrincipal varchar(100), in paginaWeb varchar(50))
	begin
		insert into Proveedores(proveedorId, nitProveedor, nombreProveedor, apellidoProveedor, direccionProveedor, razonSocial, contactoPrincipal, paginaWeb)
			values (proveedorId, nitProveedor, nombreProveedor, apellidoProveedor, direccionProveedor, razonSocial, contactoPrincipal, paginaWeb);
    End $$
Delimiter ;

call sp_AgregarProveedores(1, 'NitTodoFeo', 'Pedro', 'Vanilla', 'Atras del Ceviche', 'Ayuda', 'Tio CArajo', 'https://www.canva.com/');
call sp_AgregarProveedores(2, 'NitTodoFeo', 'Pedro', 'Vanilla', 'Atras del Ceviche', 'Ayuda', 'Tio CArajo', 'https://www.canva.com/');
call sp_AgregarProveedores(3, 'NitTodoFeo', 'Pedro', 'Vanilla', 'Atras del Ceviche', 'Ayuda', 'Tio CArajo', 'https://www.canva.com/');

Delimiter $$
	create procedure sp_AgregarCompras (in numeroDocumento int, in fechaDocumento date, in descripcion varchar(60), in totalDocumento decimal(10,2))
    begin
		insert into Compras(numeroDocumento, fechaDocumento, descripcion, totalDocumento)
        values (numeroDocumento, fechaDocumento, descripcion, totalDocumento);
    end $$
Delimiter ;

call sp_AgregarCompras(1,'2020-01-01','Un documento bien feo', 10.10);
call sp_AgregarCompras(2,'2020-01-01','Un documento bien feo', 10.10);
call sp_AgregarCompras(3,'2020-01-01','Un documento bien feo', 10.10);
call sp_AgregarCompras(4,'2020-01-01','Un documento bien feo', 10.10);

Delimiter $$
	create procedure sp_AgregarTipoProducto (in codigoTipoProducto int, descripcion varchar(45))
		begin
			insert into TipoProducto(codigoTipoProducto, descripcion)
            values (codigoTipoProducto, descripcion);
		end $$
Delimiter ;

call sp_AgregarTipoProducto(1, 'Guapo');
call sp_AgregarTipoProducto(2, 'Feo');
call sp_AgregarTipoProducto(3, 'Bonito');
call sp_AgregarTipoProducto(4, 'Pesado');
call sp_AgregarTipoProducto(5, 'Ligero');

Delimiter $$
	create procedure sp_AgregarCargoEmpleado (in codigoCargoEmpleado int, in nombreCargo varchar(45), in descripcionCargo varchar(45))
		begin
			insert into CargoEmpleado (codigoCargoEmpleado, nombreCargo, descripcionCargo)
            values (codigoCargoEmpleado, nombreCargo, descripcionCargo);
        end $$
Delimiter ;

call sp_AgregarCargoEmpleado (1, 'Carga', 'Cargar cosas');
call sp_AgregarCargoEmpleado (2, 'Caja', 'Atender');
call sp_AgregarCargoEmpleado (3, 'Conserje', 'Limpiar');


Delimiter $$
	create procedure sp_AgregarProductos (in codigoProducto int, in descripcionProducto varchar(45), in precioUnitario decimal(10,2), in precioDocena decimal(10,2), in precioMayor decimal(10,2), in existencia int, in codigoTipoProducto int, in proveedorId int)
		begin
			insert into Productos (codigoProducto, descripcionProducto, precioUnitario, precioDocena, precioMayor, existencia, codigoTipoProducto, proveedorId)
            values (codigoProducto, descripcionProducto, precioUnitario, precioDocena, precioMayor, existencia, codigoTipoProducto, proveedorId);
		end $$
Delimiter ;

call sp_AgregarProductos (1, 'Cosa horrorosa', 10.10, 10.10, 10.10, 10, 1, 1);
call sp_AgregarProductos (2, 'Cosa Buena', 9.9, 9.9, 9.9, 9, 2, 2);
call sp_AgregarProductos (3, 'Cosa Rica', 8.8, 8.8, 8.8, 8, 3, 3);

-- BUSCAR

Delimiter $$
	Create procedure sp_BuscarClientes(in cliId int)
		Begin
			Select C.clienteId,
				C.NIT,
				C.nombreCliente, 
                C.apellidoCliente,
                C.direccionCliente,
                C.telefonoCliente,
                C.correoCliente
            From Clientes C 
            where cliId = clienteId;
        End $$
Delimiter ;

call sp_BuscarClientes(2);

Delimiter $$
	create procedure sp_BuscarProveedores(in proId int)
		Begin
			Select P.proveedorId,
				P.nitProveedor,
                P.nombreProveedor,
                P.apellidoProveedor,
                P.direccionProveedor,
                P.razonSocial,
                P.contactoPrincipal,
                P.paginaWeb
			From Proveedores P
            where proId = proveedorId;
		End $$
Delimiter ;

call sp_BuscarProveedores(1);

Delimiter $$
	create procedure sp_BuscarCompras(in comId int)
		Begin
			select C.numeroDocumento,
            C.fechaDocumento,
            C.descripcion,
            C.totalDocumento
            from Compras C
            where comId = numeroDocumento;
		End $$
Delimiter ;

call sp_BuscarCompras(1);

Delimiter $$
	create procedure sp_BuscarTipoProducto(in tpId int)
		Begin
			select T.codigoTipoProducto,
            T.descripcion
            from TipoProducto T
            where tpId = codigoTipoProducto;
        End $$
Delimiter ;

call sp_buscarTipoProducto(1);

Delimiter $$
	create procedure sp_BuscarCargoEmpleado(in ceid int)
		Begin
			select CE.codigoCargoEmpleado,
            CE.nombreCargo,
            CE.descripcionCargo
            from CargoEmpleado CE
            where ceid = codigoCargoEmpleado; 
        End $$
Delimiter ;

call sp_BuscarCargoEmpleado(1);

Delimiter $$
	create procedure sp_BuscarProductos(in proid int)
		Begin
			Select PR.codigoProducto,
            PR.descripcionProducto,
            PR.precioUnitario,
            PR.precioDocena,
            PR.precioMayor,
            PR.existencia,
            PR.codigoTipoProducto,
            PR.proveedorId
            from Productos PR
            where proid = codigoProducto;
        End $$
Delimiter ;

call sp_BuscarProductos(1);

-- LISTAR

Delimiter $$
	create procedure sp_ListarClientes()
		begin
			select
            C.clienteId,
            C.NIT,
            C.nombreCliente,
            C.apellidoCliente,
            C.direccionCliente,
            C.telefonoCliente,
            C.correoCliente
            from Clientes C;
		end $$
Delimiter ;

call sp_ListarClientes();

Delimiter $$
create procedure sp_ListarProveedores()
	begin
		select * from Proveedores;
	end $$
Delimiter ;

call sp_ListarProveedores();


Delimiter $$
	create procedure sp_ListarCompras()
		begin
			select * from Compras;
		end $$
Delimiter ;

call sp_ListarCompras();

Delimiter $$
	create procedure sp_ListarTipoProducto()
		begin
			select * from TipoProducto;
        end $$
Delimiter ;

call sp_ListarTipoProducto();

Delimiter $$
	create procedure sp_ListarCargoEmpleado()
		begin
			select * from CargoEmpleado;
        end $$
Delimiter ;

call sp_ListarCargoEmpleado();

Delimiter $$
	create procedure sp_ListarProductos()
		begin
			select * from Productos;
        end $$
Delimiter ;

call sp_ListarProductos();

-- Editar
DELIMITER $$
create procedure sp_editarClientes(in clienteId int, in NIT varchar(10), in nombreCliente varchar(50), in apellidoCliente varchar(50), in direccionCliente varchar(150), in telefonoCliente varchar(8), in correoCliente varchar(45))
BEGIN
	update Clientes set
        Clientes.NIT = NIT,
        Clientes.nombreCliente = nombreCliente,
        Clientes.apellidoCliente = apellidoCliente,
        Clientes.direccionCliente = direccionCliente,
        Clientes.telefonoCliente = telefonoCliente,
        Clientes.correoCliente = correoCliente
			where Clientes.clienteId = clienteId;
END$$
DELIMITER ;

call sp_editarClientes(1,'f','fh','hf','fhf','fhgfhf','fghfghfg');

DELIMITER $$
create procedure sp_editarProveedores(in proveedorId int, in nitProveedor varchar(10), in nombreProveedor varchar(60), in apellidoProveedor varchar(60), in direccionProveedor varchar(150), in razonSocial varchar(60), in contactoPrincipal varchar(100), in paginaWeb varchar(50))
BEGIN
	update Proveedores set
    Proveedores.nitProveedor = nitProveedor,
    Proveedores.nombreProveedor = nombreProveedor,
    Proveedores.apellidoProveedor = apellidoProveedor,
    Proveedores.direccionProveedor = direccionProveedor,
    Proveedores.razonSocial = razonSocial,
    Proveedores.contactoPrincipal = contactoPrincipal,
    Proveedores.paginaWeb = paginaWeb
		where Proveedores.proveedorId = proveedorId;
END$$
DELIMITER ;

call sp_editarProveedores(2, 'YYYYGGGG', 'paaa', 'nchoooo', 'NoLoSe', 'HelpPlis', 'AAAAATTTT', 'https://www.teams.com/');


DELIMITER $$
	create procedure sp_editarCompras(in numeroDocumento int, in fechaDocumento date, in descripcion varchar(60), in totalDocumento decimal(10,2))
		BEGIN
			update Compras set
            
				Compras.numeroDocumento = numeroDocumento,
				Compras.fechaDocumento = fechaDocumento,
				Compras.descripcion = descripcion,
				Compras.totalDocumento = totalDocumento
            where Compras.numeroDocumento = numeroDocumento;
        END $$
DELIMITER ;

call sp_editarCompras(2,'1999-11-20','Otro Un documento bien feo', 8.9);

DELIMITER $$
	create procedure sp_editarTipoProducto(in codigoTipoProducto int, descripcion varchar(45))
		BEGIN
			update TipoProducto set
            TipoProducto.codigoTipoProducto = codigoTipoProducto,
            TipoProducto.descripcion = descripcion
            where TipoProducto.codigoTipoProducto = codigoTipoProducto;
        END $$
DELIMITER ;

call sp_editarTipoProducto(2, 'AA');

DELIMITER $$
	create procedure sp_editarCargoEmpleado(in codigoCargoEmpleado int, in nombreCargo varchar(45), in descripcionCargo varchar(45))
		BEGIN
			update CargoEmpleado set
            CargoEmpleado.codigoCargoEmpleado = codigoCargoEmpleado,
            CargoEmpleado.nombreCargo = nombreCargo,
            CargoEmpleado.descripcionCargo = descripcionCargo
            where CargoEmpleado.codigoCargoEmpleado = codigoCargoEmpleado;
        END $$
DELIMITER ;

call sp_editarCargoEmpleado(1, 'CosaFeo', 'Es fea y bien fea');


DELIMITER $$
	create procedure sp_editarProductos(in codigoProducto int, in descripcionProducto varchar(45), in precioUnitario decimal(10,2), in precioDocena decimal(10,2), in precioMayor decimal(10,2), in existencia int, in codigoTipoProducto int, in proveedorId int)
		BEGIN
			update Productos set
            Productos.codigoProducto = codigoProducto,
            Productos.descripcionProducto = descripcionProducto,
            Productos.precioUnitario = precioUnitario,
            Productos.precioDocena = precioDocena,
            Productos.precioMayor = precioMayor,
            Productos.existencia = existencia,
            Productos.codigoTipoProducto = codigoTipoProducto,
            Productos.proveedorId = proveedorId
			where Productos.codigoProducto = codigoProducto;
        END $$
DELIMITER ;

call sp_editarProductos(2, 'Cosa Fea', 7.7, 7.7, 7.7, 8, 2, 2);
-- BORRAR

DELIMITER $$
create procedure sp_eliminarClientes(clienteId int)
BEGIN
	delete from Clientes where clienteId = clienteId;
END$$
DELIMITER ;


DELIMITER $$
create procedure sp_eliminarProveedores(proveedorId int)
BEGIN
	delete from Proveedores where proveedorId = proveedorId;
END$$
DELIMITER ;

DELIMITER $$
	create procedure sp_eliminarCompras(numeroDocumento int)
		BEGIN
			delete from Compras where numeroDocumento = numeroDocumento;
        END $$
DELIMITER ;

DELIMITER $$
	create procedure sp_eliminarTipoProducto(codigoTipoProducto int)
		BEGIN
			delete from TipoProducto where codigoTipoProducto = codigoTipoProducto;
        END $$
DELIMITER ;

DELIMITER $$
	create procedure sp_eliminarCargoEmpleado(codigoCargoEmpleado int)
		BEGIN
			delete from CargoEmpleado where codigoCargoEmpleado = codigoCargoEmpleado;
        END $$
DELIMITER ;

DELIMITER $$
	create procedure sp_eliminarProductos(codigoProducto int)
		BEGIN
			delete from Productos where codigoProducto = codigoProducto;
        END $$
DELIMITER ;
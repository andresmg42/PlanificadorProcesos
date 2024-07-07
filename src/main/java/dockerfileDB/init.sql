DROP TABLE IF EXISTS comando;
CREATE TABLE comando(
	nombre_imagen CHAR(20) PRIMARY KEY,
	nombre_comando CHAR(20)
);

DROP TABLE IF EXISTS contenedor;
CREATE TABLE contenedor(
	contenedor_id INT PRIMARY KEY,
	nombre_imagen CHAR(20), 
	t_llegada DOUBLE PRECISION NOT NULL,
	t_estimado_ingresado DOUBLE PRECISION NOT NULL,
	t_inicial DOUBLE PRECISION ,
	t_real_estimado DOUBLE PRECISION,
	t_final DOUBLE PRECISION,
	t_turnaround_time DOUBLE PRECISION,
	t_respose_time DOUBLE PRECISION,
	
	FOREIGN KEY(nombre_imagen)REFERENCES comando(nombre_imagen)
);


DROP TABLE IF EXISTS listado;
CREATE TABLE listado(
	listado_id INT PRIMARY KEY,
	nombre CHAR(10),
	fecha DATE,
	hora TIME
);

DROP TABLE IF EXISTS c_l;	
CREATE TABLE c_l(
	contenedor_id INT, 
	listado_id INT,
	PRIMARY KEY (contenedor_id,listado_id),
	FOREIGN KEY(contenedor_id) REFERENCES contenedor(contenedor_id),
	FOREIGN KEY(listado_id) REFERENCES listado(listado_id)
);



DROP TABLE IF EXISTS ejecucion;
CREATE TABLE ejecucion(
	ejecucion_id SERIAL PRIMARY KEY,
	listado_id INT NOT NULL,
	algoritmo CHAR(10),
	fecha DATE,
	hora TIME,
	FOREIGN KEY (listado_id) REFERENCES listado(listado_id)
);

# Usa la imagen base de PostgreSQL
FROM postgres:latest

# Establece las variables de entorno necesarias para PostgreSQL
ENV POSTGRES_DB=mydatabase
ENV POSTGRES_USER=myuser
ENV POSTGRES_PASSWORD=mypassword

# Copia el script SQL en el contenedor
COPY init.sql /docker-entrypoint-initdb.d/

# Exponer el puerto de PostgreSQL
EXPOSE 5432
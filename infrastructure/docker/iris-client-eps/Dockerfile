FROM inoeg/eps:v0.1.62

COPY conf /app/settings
COPY ca /app/settings/ca

CMD ["--level", "trace", "server", "run"]

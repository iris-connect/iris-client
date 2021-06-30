FROM inoeg/eps:v0.1.32

COPY conf /app/settings
COPY ca /app/settings/ca

CMD ["--level", "trace", "server", "run"]

FROM inoeg/eps:v0.2.1

COPY conf /app/settings
COPY ca /app/settings/ca

CMD ["--level", "trace", "server", "run"]

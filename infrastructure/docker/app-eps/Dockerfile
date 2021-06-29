FROM inoeg/eps:v0.1.32

COPY conf /app/settings
COPY ca /app/settings/ca

ENV EPS_SETTINGS "settings/roles/staging/app"

CMD ["--level", "trace", "server", "run"]

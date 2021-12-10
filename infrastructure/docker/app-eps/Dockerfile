FROM inoeg/eps:v0.1.69

COPY conf /app/settings
COPY ca /app/settings/ca

ENV EPS_SETTINGS "settings/roles/test/app"

CMD ["--level", "trace", "server", "run"]

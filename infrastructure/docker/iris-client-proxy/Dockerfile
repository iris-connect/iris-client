FROM inoeg/proxy:v0.2.6

COPY conf /app/settings
COPY ca /app/settings/ca

CMD ["--level", "trace", "run", "private"]

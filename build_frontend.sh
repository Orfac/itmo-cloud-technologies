cd frontend/
docker build -t orfac/frontend:v0 .
docker tag orfac/frontend:v0 orfacello/itmo-cloud-frontend:v0
docker push orfacello/itmo-cloud-frontend:v0
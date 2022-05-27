cd frontend/
sudo docker build -t orfac/frontend:v0 .
sudo docker tag orfac/frontend:v0 orfacello/itmo-cloud-frontend:v0
sudo docker push orfacello/itmo-cloud-frontend:v0

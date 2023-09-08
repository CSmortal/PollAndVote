FROM node:18
LABEL authors="jpuazaix"

WORKDIR /app

COPY package*.json ./

RUN npm i

COPY . .

EXPOSE 80

CMD ["npm", "start"]
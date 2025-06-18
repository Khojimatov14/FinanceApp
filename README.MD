# 💸 FinanceApp

## 📋 Loyihaga qisqacha izoh

Bu loyiha foydalanuvchilarni boshqarish va ularning moliyaviy tranzaksiyalarini yuritish uchun ishlab chiqilgan oddiy REST API ilova hisoblanadi.

### 🔧 Texnologiyalar
- Java 17+
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Lombok

---

### Postman

Tayyor http so'rovlar uchun quyidagi havolaga o'ting:  
https://www.postman.com/cloudy-water-777712/financeapp/collection/y9pa5c4/financeapp-api?action=share&source=copy-link&creator=18494536

---

### Ro'yxatdan o'tish
POST https://financeapptest.up.railway.app/api/users/register  
`Content-Type: application/json`

<pre>
{  
"name": "Ali",  
"email": "ali@example.com",  
"password": 123456  
}
</pre>
---

### Login qilish
POST https://financeapptest.up.railway.app/api/users/login   
`Content-Type: application/json`

<pre>
{
    "email": "ali@example.com",
    "password": 123456
}
</pre>

---

### Token orqali foydalanuvchi malumotlarini olish
GET https://financeapptest.up.railway.app/api/users/me  
`Authorization: Bearer <Token>`

---

### Barcha foydalanuvchilar ro'yxatini olish
GET 'https://financeapptest.up.railway.app/api/users  
`Authorization: Bearer <Token>`  

---

### Foydalanuvchi hisobiga pul qo‘shish
POST https://financeapptest.up.railway.app/api/transactions  
`Authorization: Bearer <Token>`  
`Content-Type: application/json`

<pre>
{
  "userId": 1,
  "amount": 1000,
  "description": "Salary"
}
</pre>

---

### Foydalanuvchi hisobidan pul o'chirish
POST https://financeapptest.up.railway.app/api/transactions  
`Authorization: Bearer <Token>`  
`Content-Type: application/json`

<pre>
{
  "userId": 1,
  "amount": -100,
  "description": "Fine"
}
</pre>

---

### Bir foydalanuvchidan ikkinchisiga pul o'tkazish
POST https://financeapptest.up.railway.app/api/transactions/transfer  
`Authorization: Bearer <Token>`  
`Content-Type: application/json`

<pre>
{
  "fromUserId": 1,
  "toUserId": 2,
  "amount": 100
}
</pre>

---

### Foydalanuvchiga tegish barcha tranzaksiyalarini olish
GET https://financeapptest.up.railway.app/api/transactions/user/1  
`Authorization: Bearer <Token>`

---

### Sana bo'yicha tranzaksiyalarini olish
GET https://financeapptest.up.railway.app/api/transactions/date?from=2025-06-10&to=2025-06-18  
`Authorization: Bearer <Token>`

---

### Barcha tranzaksiyalar ro'yxatini olish
GET https://financeapptest.up.railway.app/api/transactions  
`Authorization: Bearer <Token>`

---
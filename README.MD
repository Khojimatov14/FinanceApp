# 💸 Moliya Dasturi (Finance App)

## 📋 Loyihaga qisqacha izoh (O‘zbek tilida)

Bu loyiha foydalanuvchilarni boshqarish va ularning moliyaviy tranzaksiyalarini yuritish uchun ishlab chiqilgan oddiy REST API ilova hisoblanadi.

### 🔧 Texnologiyalar
- Java 17+
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Lombok

### Foydalanuvchi qo‘shish
POST 'https://financeapptest.up.railway.app/api/users
Content-Type: application/json

{
"name": "Ali",
"email": "ali@example.com",
"balance": 10000
}

### Foydalanuvchini ID orqali olish
GET 'https://financeapptest.up.railway.app/api/users/1

### Barcha foydalanuvchilarni olish
GET 'https://financeapptest.up.railway.app/api/users

### Hisobga pul qo‘shish
POST 'https://financeapptest.up.railway.app/api/transactions?userId=1&amount=1000&description=Salary

### Hisobdan pul yechish
POST 'https://financeapptest.up.railway.app/api/transactions?userId=1&amount=-1000&description=Withdrawal

### Barcha tranzaksiyalarni olish
GET 'https://financeapptest.up.railway.app/api/transactions

### Foydalanuvchi tranzaksiyalarini olish
GET 'https://financeapptest.up.railway.app/api/transactions/user/1

### Sana bo‘yicha tranzaksiyalarni olish
GET 'https://financeapptest.up.railway.app/api/transactions/date?from=2025-06-01&to=2025-06-30

### Bir foydalanuvchidan boshqasiga pul o‘tkazish
POST 'https://financeapptest.up.railway.app/api/transactions/transfer?fromUserId=1&toUserId=2&amount=500&description="Transfer to friend"

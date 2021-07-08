<img src="https://raw.githubusercontent.com/AdityaV025/MuncheRestaurantPartner/master/assets/Munche%20Restaurant%20Partner%20Cover%20Page.png?token=AJM5NN7I5TJP376PFFK6Z5TA57F2W">

### Show some ❤️ and star the repo to show support for the project

<h2 align="center"> Munche Restaurant Partner Android App </h2>

<p align="center">
  <a href="LICENSE"><img alt="License" src="https://img.shields.io/badge/license-MIT-green"></a>
  <a href="https://github.com/AdityaV025/MuncheRestaurantPartner/pulls"><img alt="PRs Welcome" src="https://img.shields.io/badge/PRs-welcome-brightgreen.svg?style=flat-square"></a>
  <a href="https://github.com/AdityaV025/MuncheRestaurantPartner/pulls"><img alt="Contributions Welcome" src="https://img.shields.io/badge/contributions-welcome-brightgreen.svg?style=flat-square"></a>
</p>

This is the Munche Restaurant Partner app which is part of the Munche Customer Facing App, users can add new restaurants and accept/decline orders from the users.Setupt their UPI Payment and their restaurant's location.

# ✨ Features
- Users can add and edit menu items for different categories.
- Take payment using UPI or COD.
- Accurately setup their restaurant's location.
- Enable/disable menu items individually.
- Enable/disable their restaurant.
- and more...

# 🗒️ ToDo

- Complete the Sales page so the users can see their daily/monthly earnings.
- Build a delivery funnel and send push notifications to customers notifying them about their order status.
- Track orders and have a dedicated chat functionality.

# 📚 Major Libraries Used

- Firebase Suite - For Auth, Database and Storage
- Glide - For Image Loading.
- Lottie - To Display native `json` animations in android.

# 💻 Build Instructions

1. Clone or download the repository :

```shell
git clone https://github.com/AdityaV025/Munche.git
```

2. Import the project into Android Studio.

3. Create a firebase project and add this android app to the project.

4. Run the below command in the terminal to get your **SHA-1** key and upload it in the project settings in your firebase console, without this you cannot authenticate users using their phone numbers.

```shell
keytool -exportcert -list -v \
-alias androiddebugkey -keystore ~/.android/debug.keystore
```

5. Enable Phone Number sign in Firebase Authentication Tab in the left side.

6. Download and add the `google-services.json` file from the firebase project you created earlier and add it to the project under **app** folder.

7. Run the project into an emulator or a physical device.

# 👨 Made By

`Aditya Verma`

**Connect with me on**
</br>

[![Github](https://img.shields.io/badge/-Github-000?style=flat&logo=Github&logoColor=white)](https://github.com/AdityaV025)
[![Linkedin](https://img.shields.io/badge/-LinkedIn-blue?style=flat&logo=Linkedin&logoColor=white)](https://www.linkedin.com/in/aditya-verma-66b7a913b/)
[![Gmail](https://img.shields.io/badge/-Gmail-c14438?style=flat&logo=Gmail&logoColor=white)](mailto:aditya.verma7708@gmail.com)

# 👓 Also Checkout

<a href="https://github.com/AdityaV025/Munche">Munche Customer Facing App</a>

# 📜 License 
```
MIT License

Copyright (c) 2021 Aditya Verma

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

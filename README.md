## Cuantas Sabes / How many you know
In my first semester of biomedical engineering at ECI 'Julio Garavito', I had to develop a project which solves a problem in the medical world, due to my previous knowledge in android development I decided to build this app 'How Many you know.'

Is an app to help speech therapist with the semantic awareness and interpretation of the world things in children with less than 12 years old.

All the images were reviewed and provided by a speech therapist specialized in children.

## Project Overview
In this project the user can register as a doctor who will provide the app to their patients, so the user can register as a patient too.
The doctor will be able to:
-   get information about their patients
-   get information about the quizes realized by their patients to support their diagnosis.

## How I did it?
This project was built in Java and XML. I used so many third-party libraries to give an easy way to pick things, add a more natural way to navigate throughout the app and to improve the formulary fields.

Firebase often is the best way, with firebase I used the firebase Auth (Google, Email, and Facebook) Firebase Database and Firebase Storage to keep all the images needed for the practice of everyone based on their age.
The easiest way to get the children voice was with the Google API, probably out there are better ways, but taking into account time and money, it was the best option.

I treaded the results of the Google voice API beautifully, because the app needs to know if the child know what the image is (the semantic part), so I decided to play with the app with a few kids before the presentation, seeing a lot of possibilities for each word, like if I show them a car, they can say 'run run', 'toy', or so many others. So the app takes care of it and takes care of the possible results by the Google API.

## Screens

![screen](../master/images/2.jpg)   ![screen](../master/images/3.jpg)     ![screen](../master/images/4..jpg)

![screen](../master/images/5.jpg)   ![screen](../master/images/6.jpg)     ![screen](../master/images/7.jpg)

## License

    Copyright 2019 Joan Cabezas

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

### Problem Statement : Enhance quality of document image

- **Company Name:** Bajaj Finance Ltd.
- **Website:** https://www.bajajfinserv.in/

- In our app, users / customers upload various documents images to fulfil KYC / Onboarding / File processing requirements.

- Develop an in-app tool to enhance quality of a document image - clicked, scanned or uploaded from gallery to app.

### Capabilities to include by not limited to:

- Auto or Slider Based Colour / Contrast enhancement
- Grey scales reduction in case of scanned images with grey hue or ink tome issues
- Skew & tilt correction
- 360-degree rotation
- Border identification & correction,
- Cropping etc.

### Evaluation/Acceptance criterion:

- Solution should be able to execute most of the able corrections on its own to the document image once uploaded by clicking Process Document button or some of them manually depending on what type of image enhancement/correction is required, with provision to rollback / redo changes.

### Some Industry reference examples:

- CamScanner, TapScanner, Photo editing capabilities in gallery section of most android phones etc..

### Project Structure:
  The whole project is divided into 3 modules
- __Module 1__ - [_:app_] => Holds the small part _Bajaj finserv app_ User Interface.
- __Module 2__ - [_:docscanner_] => The image editing capabilities will lie in this module.
- __Module 3__ - [_:openCVLibrary_] => The automatic edge detection and cropping like functionalities which require Machine Learning.

    ````
    - [:app]
        - com.aviza.finserv
            - HomeActivity
            - MainActivity . . . and various other Activities
    - [:docscanner]
        - com.aviza.docscanner
            - base
                - CropperErrorType
                - DocumentScanActivity
            - helpers
                - ImageUtils
                - MathsUtils
                - ScannerConstants
            - libraries
                - NativeClass
                - PerspectiveTransformation
                - PolygonView
        - ImageCropActivity
    - [:openCVLibrary]
        - org.opencv
            - android . . . and various other packages.
    - Gradle Scripts
        - Build.gradle . . . and various other module level gradles as well as - properties.
    ````    
    
### Work Flow:

- __HomeActivity__ - When the application starts, the user will see this Activity. This Activity actually contains a small part of the user interface of the “Bajaj Finserv” application, where we added a CardView of “__KYC Verification__”.
- __KYCVerificationActivity__ - As the user clicks on the __VERIFY__ button it takes the user this activity. In this Activity user required to fill the basic details need for KYC purpose, we have a small forms for the same as the company need more user data we add more fields their, for the development purpose we are only using some basic details only, we can also include a __OTP verification__ field in this activity which will be send to the mobile number which linked to the Aadhaar Number. After filling the basic details, the user will have to choose whether he/she would Capture the document image or choose from Gallery.
- __Camera Button__ -  The user clicks on the camera button, it will open the camera’s intent then the user has to click the document image as a normal snapshot. As soon as the picture is clicked the user will be redirected to a __ImageCropperActivity__.
- __Gallery Button__ -  The user clicks on the camera button, it will open the gallery’s intent user will select the required document. As soon as the picture is clicked the user will be redirected to a ImageCropperActivity.
- __ImageCropperActivity__ - In this Activity, users will have to use the various document editing options or proceed with the default magic color which is done by Machine Learning which resides in the __openCVLibrary__. After trimming the document the user will be redirected to the KYCVerification activity, where they can see a small thumbnail of their document. After doing all the process, there is a __DONE Button__ in the bottom of the screen.
- __Done Button__ - As soon as user clicks on the __Done Button__, for sake of completeness we check that users has really filled the details, if all thing goes right the details of the user is sended to the Firebase Cloud Storage (or in production Bajaj Finserv Backend) along with the document image. Which can be used by the administration for further verification. This is the whole working of our “__Aviza - Bajaj Finserv__”.
- __SplashScreen__ - Just for sake of better user interface we have implemented a splash screen which is shown during the start-up of the application.


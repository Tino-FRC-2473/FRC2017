# FRC2017

        If you already have Eclipse and WPILIB, skip to step 3.

Setup:

1. First, if you have not already done so, you need to download Eclipse Neon.2. There are a couple of different versions, based off of what you need, ex. web development, but here is the link to the standard download. [Mac](https://www.eclipse.org/downloads/download.php?file=/oomph/epp/neon/R2a/eclipse-inst-mac64.tar.gz), [PC](http://www.eclipse.org/downloads/packages/eclipse-ide-java-developers/neon2) (Choose the appropriate download link for your pc. (32 bit vs. 64 bit))

2. Download and install wpilib software on Eclipse.

    a. Open Eclipse.

    b. Click on Help -> Install New Software. At this point, a new window should open, that looks like this. ![Alt text](InstallNewSoftware.png?raw=true "Install New Software")
    c. Click on Add... 

    d. You should be taken to this, other, window. 
    ![Alt text](AddSoftware.png?raw=true "Add Software")

    e. For Name, type in something like FRC or WPILIB, so that you know that it is the software for robotics.

    f. For Location, paste in the following URL: [WPILIB Software](http://first.wpi.edu/FRC/roborio/release/eclipse/)

    g. Click OK, and then install the software. Make sure to only install the software for Java Development, because the C/C++ Software is not neccessary for us.
3. Download the CTRE 3rd Party Library.

    a. [Windows Users](http://www.ctr-electronics.com/downloads/installers/CTRE%20Toolsuite%20v4.4.1.8.zip) Click on the link to download the installer, and follow its instructions.

    b. [Mac Users](http://www.ctr-electronics.com//downloads/lib/CTRE_FRCLibs_NON-WINDOWS.zip) Click on the link to download a zip file. If you are using Safari, it will automatically unzip to reveal a folder. If you are not using Safari, double click on the .zip file to open it.

        1. After unzipping you should see a folder called CTRE_FRCLibs_NON-WINDOWS. Click on this folder.

        2. You should now see several folders. One of them will be called "java".

        3. Copy this folder.

        4. Open up Terminal (or whatever you use in it's place (ex. iTerm)')

        5. Type the following commands EXACTLY:
                cd
                cd wpilib
                cd user
                open .
    

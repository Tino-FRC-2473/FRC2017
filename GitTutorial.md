#Git Tutorial
##Setup (EVERYONE MUST READ THIS)
1. The first thing to do is to create your FRC2017 project. Go to Eclipse, then select File -> New -> Project... -> Robot Java Project. Fill out all the required information, such as Team Number. For Project Name, type in the name of the repo, in this case, <b>FRC2017</b>. The following popup should open. For the menu items that you should select, check the upper right corner of the image to see where my mouse is.
2. If you are a Mac user, and have HomeBrew or MacPorts installed, skip this step. If you have Scoop, or a similar Package Manager for Windows installed, skip this step. If not, follow the instructions, because they will guide you in the installation of a Package Manager, which is useful software that allows you to install lots of software like git, OpenCV, Python, etc., with only a few commands. [PC Users](#pc). [Mac Users Macports](#mp) [Mac Users HomeBrew](#hb)

    a. <a name="pc"></a>PC Users: Navigate to the Command Line app. Type the following phrase exactly, and hit enter. <b>$PSVersionTable.PSVersion</b> This will tell you if you have PowerShell installed.
       
    ####If the version isn't 3.0, click this link: [PowerShell 3.0](https://www.microsoft.com/en-us/download/confirmation.aspx?id=34595), and follow the instructions to install PowerShell 3.0.
    b. Now type <b>set-executionpolicy unrestricted -s cu</b> exactly, to give permission to PowerShell 3.0 to work.
    c. Now, all you have to do is type <b>iex (new-object net.webclient).downloadstring('https://get.scoop.sh')</b> exactly. This will download scoop.
    d. You can now use scoop to install some handy software. The recommendations are curl (Downloads whatever is at the specified URL), coreutils, git, grep, openssh, sed, and wget (which is the same as curl, but is better for some things). To install all of this software, type <b>scoop install coreutils curl git grep sed wget</b>. This should conclude the installation of Scoop. <b><strong>SKIP TO STEP 3</b></strong>
    e. <a name="mp"></a>Before you install anything, you need to install Xcode from the App Store.
    f. Next, open Terminal, or your command line app, and type the following: <b>sudo xcodebuild -license</b> To view and accept the Xcode license. Add <b>accept</b> to the previous command to automatically accept it.
    g. Still within Bash, how I will reference Terminal from now on, hit the following command: <b>xcode-select --install</b>.
    h. If you have macOS Sierra, click on this [link](https://github.com/macports/macports-base/releases/download/v2.3.5/MacPorts-2.3.5-10.12-Sierra.pkg) to automatically download and begin the process of install MacPorts. If you have an older OS, follow this link, and click the appropriate link to download and install MacPorts. This should conclude the installation of MacPorts.<b><strong>SKIP TO STEP 3</b></strong>
    i. <a name="hb"></a>Before you install anything, you need to install Xcode from the App Store.
    j. Next, open Terminal, or your command line app, and type the following: <b>sudo xcodebuild -license</b> To view and accept the Xcode license. Add <b>accept</b> to the previous command to automatically accept it.
    k. Still within Bash, how I will reference Terminal from now on, hit the following command: <b>xcode-select --install</b>.
    l. Still within Bash, type the following command: <b>/usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"</b> This should conclude the installation of HomeBrew.<b><strong>SKIP TO STEP 3</b></strong>
3. Now, you must download git. Type command for [Scoop](), [MacPorts](), or [HomeBrew]().
    a. <a name="sg"></a> <b><strong>SKIP THIS STEP IF YOU RAN THE SCOOP COMMAND IN 2B</b></strong><b>scoop install git</b>.
    b. <a name="mpg"></a> Type this the following commands:
        sudo port selfupdate (Updates the ports)
        port search git-core
        port variants git-core
        sudo port install git-core+bash_completion+svn+doc+gitweb
        git config --global user.name "your user name"
        git config --global user.email "your email address"
        git config --global color.branch auto
        git config --global color.diff auto
        git config --global color.interactive auto
        git config --global color.status auto
    c. <a name="hbg"></a> <b>brew install git</b>
4. Now, we have to initialize the repository in your project. Using bash, navigate to your <b>FRC2017</b> project file. For example, I would type the following command: <b>cd /Users/RehanDurrani/Rehan\ Durrani\ Java\ Workspace/FRC2017</b>.
5. Type the following command to initialize the file: <b>git init</b>.
6. Next, type this: <b>git clone https://github.com/Tino-FRC-2473/FRC2017.git</b>. The way I got the link was by navigating to the GitHub page, clicking on the "Clone or download" green button, and copy-pasting the link.
7. You now have all the files, and the GitHub repository. Read on to find more about your daily workflow.

##Daily Workflow (EVERYONE MUST READ THIS)


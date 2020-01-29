# Setup Guide
This document describe how to follow proper git branching model for large scale projects and how to automate some of mundane tasks for manually maintaining git brances.

Git has most successful branching model for large scale project ,which is provided by Vincent Driessen as part of [this](https://nvie.com/posts/a-successful-git-branching-model/) document.
On top of this if we want to automate this process then there is Maven plugin named [gitflow-maven-plugin](https://github.com/aleksandr-m/gitflow-maven-plugin), which automates following things.

-   Auto generation of feature, release and hotfix branches using simple Maven commands.
-   Automatic version control of pom files.
-   Automatic git tag creation.

From now onwards I would describe how to use **gitflow-maven-plugin** assuming that you have referred to git branching strategy in link given above.

### How to add gitflow-maven-plugin
In pom.xml file please add following snippet. This plugin is fully compliant with Apache licence.

```xml
     <build>
            <plugins>
                <plugin>
                    <groupId>com.amashchenko.maven.plugin</groupId>
                    <artifactId>gitflow-maven-plugin</artifactId>
                    <version>1.14.0</version>
                    <configuration>
                        <installProject>false</installProject>
                        <verbose>false</verbose>
                        <keepBranch>true</keepBranch>
                        <skipTestProject>true</skipTestProject>
                        <installProject>false</installProject>
                        <verbose>false</verbose>
                        <gitFlowConfig>
                            <productionBranch>master</productionBranch>
                            <developmentBranch>develop</developmentBranch>
                            <featureBranchPrefix>feature_</featureBranchPrefix>
                            <releaseBranchPrefix>release_</releaseBranchPrefix>
                            <hotfixBranchPrefix>hotfix_</hotfixBranchPrefix>
                            <origin>origin</origin>
                        </gitFlowConfig>
                        <commitMessages>
                            <!-- since 1.2.1, see Customizing commit messages -->
                        </commitMessages>
                    </configuration>
                </plugin>
            </plugins>
        </build>
```

Following things we require to take care while setting up this plugin.
-   Under **gitFlowConfig** please make sure that you give correct branch name and also prefix which you follow in your organization.
-   If **keepBranch** is true then plugin will not delete branch after merging.

Now following steps we require to follow for generation of branch and merging the branch.

### Feature branch
-   To create feature branch automatically first you need to checkout develop branch.
-   Run below command
    ```shell script
      mvn gitflow:feature-start
    ```
-   This command asks you to provide feature branch name and it will create the branch.
-   You can push that branch to remote and create pull request against develop branch to review and merge.
-   To revise entire flow following are list of activities to do in sequence.
    -   Create feature branch
        ```shell script
        $ mvn gitflow:feature-start
        [INFO] Scanning for projects...
        [INFO]
        [INFO] ----------------------< com.examples:git-release >----------------------
        [INFO] Building git-release 1.0.0.8-SNAPSHOT
        [INFO] --------------------------------[ jar ]---------------------------------
        [INFO]
        [INFO] --- gitflow-maven-plugin:1.14.0:feature-start (default-cli) @ git-release ---
        [INFO] Checking for uncommitted changes.
        [INFO] Fetching remote branch 'origin develop'.
        [INFO] Comparing local branch 'develop' with remote 'origin/develop'.
        What is a name of feature branch? feature_: myNewFeature
        [INFO] Creating a new branch 'feature_myNewFeature' from 'develop' and checking it out.
        [INFO] Updating version(s) to '1.0.0.8-myNewFeature-SNAPSHOT'.
        [INFO] Committing changes.
        [INFO] ------------------------------------------------------------------------
        [INFO] BUILD SUCCESS
        [INFO] ------------------------------------------------------------------------
        [INFO] Total time:  28.172 s
        [INFO] Finished at: 2020-01-29T19:09:55+05:30
        [INFO] ------------------------------------------------------------------------
        ```
    -   Push feature branch to remote git server.
        ``` shell script
        $ git push origin feature_: myNewFeature
        ```
    -   Create, Review and Merge pull request between feature and develop branch.
    -   Delete feature branch.
        ``` shell script
        $ git branch -D feature_: myNewFeature
        ```



### Release branch
-   To  create release branch automatically you need to checkout develop branch.
-   Run below command
    ```shell script
      mvn gitflow:release-start
    ```
-   As part of this command it will create release branch and it will generate release version from snapshot version automatically.
-   Push this release branch to remove git server and create pull request between release and master branch.
-   Once pull request is reviewed, changes can be merged to master branch.
-   Now once release branch is merged we require to generate git tag automatically, for which run below command.
     ```shell script
      mvn gitflow:release-finish
    ```
-   Above command will automatically create git tag.
-   After this we can delete release branch.
-   To revise entire flow following are list of activities to do in sequence.
    -   Create release branch
        ```shell script
        $ mvn gitflow:release-start
        [INFO] Scanning for projects...
        [INFO]
        [INFO] ----------------------< com.examples:git-release >----------------------
        [INFO] Building git-release 1.0.0.9-SNAPSHOT
        [INFO] --------------------------------[ jar ]---------------------------------
        [INFO]
        [INFO] --- gitflow-maven-plugin:1.14.0:release-start (default-cli) @ git-release ---
        [INFO] Checking for uncommitted changes.
        [INFO] Fetching remote branch 'origin develop'.
        [INFO] Comparing local branch 'develop' with remote 'origin/develop'.
        [INFO] Checking out 'develop' branch.
        [INFO] Checking for SNAPSHOT versions in dependencies.
        What is release version? [1.0.0.9]:
        [INFO] Version is blank. Using default version.
        [INFO] Creating a new branch 'release_1.0.0.9' from 'develop' and checking it out.
        [INFO] Updating version(s) to '1.0.0.9'.
        [INFO] Committing changes.
        [INFO] ------------------------------------------------------------------------
        [INFO] BUILD SUCCESS
        [INFO] ------------------------------------------------------------------------
        [INFO] Total time:  22.408 s
        [INFO] Finished at: 2020-01-29T19:14:32+05:30
        [INFO] ------------------------------------------------------------------------
        ```
    -   Push release branch to remote git server
        ```shell script
         $ git push origin release_1.0.0.9
        ```
    -   Create, Review and Merge pull request between release and master branch.
    -   Checkout master and pull latest changes
        ```shell script
        $ git checkout master
        $ git pull
        ```
    -   Finish release to create tag and also bump develop branch pom version
        ```shell script
        $ mvn gitflow:release-finish
        [INFO] Scanning for projects...
        [INFO]
        [INFO] ----------------------< com.examples:git-release >----------------------
        [INFO] Building git-release 1.0.0.9
        [INFO] --------------------------------[ jar ]---------------------------------
        [INFO]
        [INFO] --- gitflow-maven-plugin:1.14.0:release-finish (default-cli) @ git-release ---
        [INFO] Checking for uncommitted changes.
        [INFO] Checking out 'release_1.0.0.9' branch.
        [INFO] Checking for SNAPSHOT versions in dependencies.
        [INFO] Fetching remote branch 'origin release_1.0.0.9'.
        [INFO] Comparing local branch 'release_1.0.0.9' with remote 'origin/release_1.0.0.9'.
        [INFO] Fetching remote branch 'origin develop'.
        [INFO] Comparing local branch 'develop' with remote 'origin/develop'.
        [INFO] Fetching remote branch 'origin master'.
        [INFO] Comparing local branch 'master' with remote 'origin/master'.
        [INFO] Checking out 'release_1.0.0.9' branch.
        [INFO] Checking out 'master' branch.
        [INFO] Merging (--no-ff) 'release_1.0.0.9' branch.
        [INFO] Creating '1.0.0.9' tag.
        [INFO] Checking out 'develop' branch.
        [INFO] Merging (--no-ff) 'release_1.0.0.9' branch.
        [INFO] Updating version(s) to '1.0.0.10-SNAPSHOT'.
        [INFO] Committing changes.
        [INFO] Pushing 'master' branch to 'origin'.
        [INFO] Pushing 'develop' branch to 'origin'.
        [INFO] ------------------------------------------------------------------------
        [INFO] BUILD SUCCESS
        [INFO] ------------------------------------------------------------------------
        [INFO] Total time:  34.690 s
        [INFO] Finished at: 2020-01-29T19:19:11+05:30
        [INFO] ------------------------------------------------------------------------
        ```
    -   Delete release branch
        ```shell script
        $ git branch -D release_1.0.0.9
        ```
### Hotfix branch
-    To  create release branch automatically you need to checkout master branch.
-   Run below command
    ``` shell script
      mvn gitflow:hotfix-start
    ```
-   It creates local hotfix branch hence you would require to push that hotfix to remote git server and create pull request between hotfix and master branch.
-   Once pull request is reviewed, changes can be merged to master branch and after that you can delete hotfix branch.
-   Now we also require to create git tag and also propagate hotfix change back to develop, hence we require to execute following command.
    ```
      mvn gitflow:hotfix-finish
    ```
-   This command will merge hotfix branch to develop branch. After merge only manual step is to bump develop branch version
-   To revise entire flow following are list of activities to do in sequence.
    -   Create hotfix branch
        ```shell script
        $ mvn gitflow:hotfix-start
        [INFO] Scanning for projects...
        [INFO]
        [INFO] ----------------------< com.examples:git-release >----------------------
        [INFO] Building git-release 1.0.0.11
        [INFO] --------------------------------[ jar ]---------------------------------
        [INFO]
        [INFO] --- gitflow-maven-plugin:1.14.0:hotfix-start (default-cli) @ git-release ---
        [INFO] Checking for uncommitted changes.
        [INFO] Checking out 'master' branch.
        [INFO] Fetching remote branch 'origin master'.
        [INFO] Comparing local branch 'master' with remote 'origin/master'.
        What is the hotfix version? [1.0.0.12]:
        [INFO] Version is blank. Using default version.
        [INFO] Creating a new branch 'hotfix_1.0.0.12' from 'master' and checking it out.
        [INFO] Updating version(s) to '1.0.0.12'.
        [INFO] Committing changes.
        [INFO] ------------------------------------------------------------------------
        [INFO] BUILD SUCCESS
        [INFO] ------------------------------------------------------------------------
        [INFO] Total time:  15.308 s
        [INFO] Finished at: 2020-01-29T19:38:06+05:30
        [INFO] ------------------------------------------------------------------------
        ```
    -  Push hotfix branch to remote git server
        ```shell script
        $ git push origin hotfix_1.0.0.12
        ```
    -   Create, Review and Merge pull request between hotfix and master branch.
    -   Checkout master and pull changes.
        ```shell script
        $ git checkout master
        $ git pull
        ```
    -   Create git tag by finishing hotfix and also merge changes to develop.
        ```shell script
        $ mvn gitflow:hotfix-finish
        [INFO] Scanning for projects...
        [INFO]
        [INFO] ----------------------< com.examples:git-release >----------------------
        [INFO] Building git-release 1.0.0.12
        [INFO] --------------------------------[ jar ]---------------------------------
        [INFO]
        [INFO] --- gitflow-maven-plugin:1.14.0:hotfix-finish (default-cli) @ git-release ---
        [INFO] Checking for uncommitted changes.
        Hotfix branches:
        1. hotfix_1.0.0.12
        Choose hotfix branch to finish (1): 1
        [INFO] Fetching remote branch 'origin hotfix_1.0.0.12'.
        [INFO] Comparing local branch 'hotfix_1.0.0.12' with remote 'origin/hotfix_1.0.0.12'.
        [INFO] Fetching remote branch 'origin develop'.
        [INFO] Comparing local branch 'develop' with remote 'origin/develop'.
        [INFO] Fetching remote branch 'origin master'.
        [INFO] Comparing local branch 'master' with remote 'origin/master'.
        [INFO] Checking out 'hotfix_1.0.0.12' branch.
        [INFO] Checking out 'master' branch.
        [INFO] Merging (--no-ff) 'hotfix_1.0.0.12' branch.
        [INFO] Creating '1.0.0.12' tag.
        [INFO] Checking out 'develop' branch.
        [INFO] Updating version(s) to '1.0.0.12'.
        [INFO] Committing changes.
        [INFO] Merging (--no-ff) 'hotfix_1.0.0.12' branch.
        [INFO] Updating version(s) to '1.0.0.12-SNAPSHOT'.
        [INFO] Committing changes.
        [INFO] Pushing 'master' branch to 'origin'.
        [INFO] Pushing 'develop' branch to 'origin'.
        [INFO] ------------------------------------------------------------------------
        [INFO] BUILD SUCCESS
        [INFO] ------------------------------------------------------------------------
        [INFO] Total time:  41.809 s
        [INFO] Finished at: 2020-01-29T19:42:07+05:30
        [INFO] ------------------------------------------------------------------------
        ```
    -   Delete hotfix branch
        ```shell script
        $ git branch -D hotfix_1.0.0.10
        ```








    
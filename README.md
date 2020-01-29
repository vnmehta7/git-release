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
-   This command generates git tag with hotfix version and creates interim release branch which has snapshot version.
-   Now you need to create pull request between interim release branch and develop branch.
-   Once changes in pull request are reviewed and merged, you can delete hotfix and interim release branch.







    
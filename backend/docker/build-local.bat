rem Set project variables
set PROJECT_NAME=%1
set PROJECT_TAG=%2

rem Find Docker command
set TEMP_DOCKER_CMD_FILE=docker-cmd.txt
where docker >> %TEMP_DOCKER_CMD_FILE%
set /p DOCKER_CMD=<%TEMP_DOCKER_CMD_FILE%
del %TEMP_DOCKER_CMD_FILE%

rem If tag not specified, use currently active git branch as tag
if not "!PROJECT_TAG!" == "" (
  rem Find Git command
  set TEMP_GIT_CMD_FILE=git-cmd.txt
  where git >> %TEMP_GIT_CMD_FILE%
  set /p GIT_CMD=<%TEMP_GIT_CMD_FILE%
  del %TEMP_GIT_CMD_FILE%

  rem Find Git branch
  set TEMP_GIT_BRANCH_FILE=git-branch.txt
  %GIT_CMD% rev-parse --abbrev-ref HEAD >> %TEMP_GIT_BRANCH_FILE%
  set /p PROJECT_TAG=<%TEMP_GIT_BRANCH_FILE%
  del %TEMP_GIT_BRANCH_FILE%
)

rem Build to local Docker registry
%DOCKER_CMD% build --force-rm=true -t %PROJECT_NAME%:%PROJECT_TAG% -t %PROJECT_NAME%:latest --build-arg version=latest ./
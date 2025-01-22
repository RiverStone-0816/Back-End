# **Jenkins 정리 문서**

## **목차**

1. **Jenkins 개요**
   - 1.1 Jenkins 개요
   - 1.2 CentOS 7에서 Java 17 설치
   - 1.3 CentOS 7에서 Jenkins 설치
   - 1.4 원활한 배포를 위한 Jenkins SSH 접속 설정
2. **Jenkins를 위한 Git Hook 설정**
   - 2.1 Jenkins용 Git Hook 설정
   - 2.2 웹훅 데이터 활용
   - 2.3 조건부 배포 자동화를 위한 Pipeline 구성 및 사용 방법
   - 2.4 사용 가이드라인

---

## **1. Jenkins 개요**

### **1.1 Jenkins 개요**

Jenkins는 소프트웨어 개발 프로세스를 자동화하는 오픈 소스 지속적 통합(CI) 및 지속적 배포(CD) 도구입니다. 2004년 코스케 가와구치(Kohsuke Kawaguchi)가 Sun Microsystems에서 Hudson이라는 이름으로 개발을 시작했으며, 2011년 오라클과의 상표권 분쟁으로 인해 Jenkins로 이름이 변경되었습니다.

#### **주요 기능**

- **자동화된 빌드 및 테스트**: 코드가 저장소에 푸시될 때마다 자동으로 빌드를 실행하고 테스트를 수행.
- **지속적 통합(CI)**: 여러 개발자의 작업을 주기적으로 통합하여 충돌을 조기에 발견하고 해결.
- **지속적 배포(CD)**: 테스트를 통과한 코드를 자동으로 스테이징 또는 프로덕션 환경에 배포.
- **플러그인 생태계**: 1500개 이상의 플러그인을 통해 다양한 개발 도구와 통합 가능.
- **분산 빌드**: 여러 시스템에 걸쳐 빌드 작업을 분산시켜 처리 속도를 높임.
- **파이프라인 지원**: 복잡한 빌드 및 배포 프로세스를 코드로 정의하고 관리 가능.

### **1.2 CentOS 7에서 Java 17 설치**

Jenkins는 Java로 작성된 도구로, Java 런타임 환경이 필요합니다. 최신 Jenkins 버전은 Java 17 이상을 요구합니다.

#### **설치 방법 (CentOS 7 기준)**

1. **Java 패키지 저장소 추가**:
   
   ```bash
   sudo yum install -y wget
   sudo wget https://download.oracle.com/java/17/latest/jdk-17_linux-x64_bin.rpm
   ```

2. **Java 설치**:
   
   ```bash
   sudo rpm -ivh jdk-17_linux-x64_bin.rpm
   ```

3. **Java 버전 확인**:
   
   ```bash
   java -version
   ```

### **1.3 CentOS 7에서 Jenkins 설치**

#### **설치 방법**

1. **Jenkins 저장소 추가**:
   
   ```bash
   sudo wget -O /etc/yum.repos.d/jenkins.repo https://pkg.jenkins.io/redhat-stable/jenkins.repo
   sudo rpm --import https://pkg.jenkins.io/redhat-stable/jenkins.io.key
   ```

2. **필수 패키지 설치**:
   
   ```bash
   sudo yum install epel-release java-11-openjdk-devel git -y
   ```

3. **Jenkins 설치**:
   
   ```bash
   sudo yum install jenkins -y
   ```

4. **Jenkins 서비스 시작 및 활성화**:
   
   ```bash
   sudo systemctl start jenkins
   sudo systemctl enable jenkins
   ```

5. **방화벽 설정 (포트 8080 열기)**:
   
   ```bash
   sudo firewall-cmd --permanent --add-port=8080/tcp
   sudo firewall-cmd --reload
   ```

6. **Jenkins 초기 비밀번호 확인**:
   
   ```bash
   sudo cat /var/lib/jenkins/secrets/initialAdminPassword
   ```

7. 브라우저에서 `http://<서버_IP>:8080`으로 접속 후 초기 비밀번호 입력.

### **1.4 원활한 배포를 위한 Jenkins SSH 접속 설정**

Jenkins에서 원격 서버로 파일을 전송하거나 명령어를 실행하려면 SSH 키 기반 인증을 설정해야 합니다.

#### **SSH 키 생성 및 설정**

1. SSH 키 생성 (Jenkins 서버에서):
   
   ```bash
   ssh-keygen -t rsa -b 4096 -C "jenkins@yourdomain.com"
   ```

2. 공개 키를 원격 서버에 추가:
   
   ```bash
   ssh-copy-id root@<REMOTE_SERVER_IP>
   ```

3. SSH 접속 테스트:
   
   ```bash
   ssh root@<REMOTE_SERVER_IP>
   ```

4. Jenkins에서 SSH 키 설정:
   
   - Jenkins 관리 > "크리덴셜" > "시스템" > "Global credentials" > 추가.
   - 유형: "SSH Username with private key".
   - 사용자 이름: `root`.
   - 프라이빗 키: `id_rsa` 파일 내용 복사.

## **2. Jenkins를 위한 Git Hook 설정**

### **2.1 Jenkins용 Git Hook 설정**

Git의 `post-receive` Hook은 Push 이벤트 발생 후 실행되는 스크립트입니다. 이를 활용하여 Jenkins Webhook과 연동할 수 있습니다.

#### **Git Hook 코드**

```bash
#!/bin/bash

export PATH=/usr/bin:/bin:/usr/local/bin:$PATH

while read oldrev newrev refname; do
    # 디버깅 로그 추가
    echo "Old Revision: $oldrev" >> /opt/git_pushlog/common_post-receive-debug.log
    echo "New Revision: $newrev" >> /opt/git_pushlog/common_post-receive-debug.log
    echo "Reference Name: $refname" >> /opt/git_pushlog/common_post-receive-debug.log

    # 작성자 이름 추출 (가장 최근 커밋의 작성자)
    AUTHOR_NAME=$(git log -1 --pretty=format:'%an' $newrev)

    # 커밋 메시지 추출
    COMMIT_MESSAGE=$(git log -1 --pretty=format:'%s' $newrev)

    # 태그인지 확인 (refs/tags/로 시작하는 경우)
    if [[ "$refname" == refs/tags/* ]]; then
        TAG_NAME=${refname#refs/tags/}  # refs/tags/ 제거하여 태그 이름만 추출

        # 브랜치 이름 추출 (첫 번째 브랜치만 선택)
        BRANCH_NAME=$(git branch --contains $newrev | grep -v "detached" | sed 's/* //' | head -n 1)
        BRANCH_NAME=${BRANCH_NAME:-unknown}  # 비어 있으면 "unknown"으로 설정

        # 안전한 브랜치 이름 생성 (슬래시를 언더스코어로 변환)
        SAFE_BRANCH_NAME=$(echo "$BRANCH_NAME" | sed 's|/|_|g' | xargs)

        echo "New Revision: $newrev" >> /opt/git_pushlog/common_post-receive-debug.log
        echo "Commit Message: $COMMIT_MESSAGE" >> /opt/git_pushlog/common_post-receive-debug.log

        # 커밋 메시지에서 Release IP 추출 (@Releaseip:XXX.XXX.XXX.XXX)
        IP_ADDRESS=$(echo "$COMMIT_MESSAGE" | grep -oP "@releaseip:['\"]?[0-9]+\.[0-9]+\.[0-9]+\.[0-9]+['\"]?" | sed -E "s/@releaseip:['\"]?([0-9]+\.[0-9]+\.[0-9]+\.[0-9]+)['\"]?/\1/")
        IP_ADDRESS=${IP_ADDRESS:-"N/A"}  # 비어 있으면 "N/A"로 설정

        # 커밋 메시지에서 Front Type 추출 (@fronttype:'LGT')
        FRONT_TYPE=$(echo "$COMMIT_MESSAGE" | grep -oP "@fronttype:['\"]?[A-Za-z]+['\"]?" | sed -E "s/@fronttype:['\"]?([A-Za-z]+)['\"]?/\1/")
        FRONT_TYPE=${FRONT_TYPE:-"N/A"}  # 비어 있으면 "N/A"로 설정

        # 로그 파일 경로 설정 및 디렉터리 생성
        LOG_FILE="/opt/git_pushlog/$SAFE_BRANCH_NAME/post-receive.log"
        mkdir -p "/opt/git_pushlog/$SAFE_BRANCH_NAME"

         # 현재 시간 가져오기
        CURRENT_TIME=$(date '+%Y-%m-%d %H:%M:%S')

        # JSON 데이터 생성 (태그, 브랜치, 작성자, Release IP, Front Type 포함)
        JSON_DATA=$(printf '{"branch": "%s", "name": "%s", "tag": "%s", "releaseip": "%s", "fronttype": "%s"}' "$BRANCH_NAME" "$AUTHOR_NAME" "$TAG_NAME" "$IP_ADDRESS" "$FRONT_TYPE")

        # JSON 데이터 유효성 검사
        if [[ -z "$JSON_DATA" ]]; then
            echo "[$CURRENT_TIME] JSON 데이터 생성 실패: 태그 $TAG_NAME, 작성자 $AUTHOR_NAME, 브랜치 $SAFE_BRANCH_NAME" >> $LOG_FILE
            exit 1
        fi

        # JSON 데이터 로그 찍기
        echo "[$CURRENT_TIME] JSON_DATA: $JSON_DATA" >> $LOG_FILE
	

	# 
        # Jenkins Webhook 호출 (태그용)
        curl -X POST -H "Content-Type: application/json" \
             -d "$JSON_DATA" \
             http://122.49.74.5:25115/generic-webhook-trigger/invoke?token=ZWljbi1qZW5raW5zLWRldi10b2tlbg== \
             >> $LOG_FILE 2>&1

        if [[ $? -ne 0 ]]; then
            echo "[$CURRENT_TIME] Webhook 호출 실패: 태그 $TAG_NAME, 작성자 $AUTHOR_NAME, 브랜치 $SAFE_BRANCH_NAME, ReleaseIP $IP_ADDRESS, FrontType $FRONT_TYPE" >> $LOG_FILE
            echo "[$CURRENT_TIME] HTTP 응답 코드: $(curl -s -o /dev/null -w "%{http_code}" http://122.49.74.5:25115/generic-webhook-trigger/invoke?token=eicn-webhook-tokentest)" >> $LOG_FILE
        else
            echo "[$CURRENT_TIME] Webhook 호출 성공: 태그 $TAG_NAME, 작성자 $AUTHOR_NAME, 브랜치 $SAFE_BRANCH_NAME, ReleaseIP $IP_ADDRESS, FrontType $FRONT_TYPE" >> $LOG_FILE
        fi

        break
    fi

done

# 오래된 로그 삭제 (30일 이상 된 파일)
find /opt/git_pushlog/ -type f -name "*.log" -mtime +30 -exec rm {} \


```

### **2.2 웹훅 데이터 활용**

#### **Webhook JSON 데이터 예시**

```json
{
    "branch": "chatbot_jenkins",
    "name": "강현우",
    "tag": "v1.0.0",
    "releaseip": "122.49.74.102",
    "fronttype": "LGT"
}
```

### **2.3 조건부 배포 자동화를 위한 Pipeline 구성 및 사용 방법**

#### **파라미터화된 Jenkins 파이프라인 코드**

```groovy
pipeline {
    agent any
    parameters {
        string(name: 'branch', defaultValue: 'chatbot', description: 'Git 브랜치 이름')
        string(name: 'name', defaultValue: '', description: '작성자 이름')
        string(name: 'tag', defaultValue: '', description: 'Git 태그 이름')
        string(name: 'releaseip', defaultValue: '', description: '릴리즈 IP 주소')
        string(name: 'fronttype', defaultValue: '', description: 'Frontend Type (LGT or SKT)')
    }
    environment {
        REPO_URL = "/opt/git/premium/skt-premuim/premuim.git"  // Git 저장소 URL
        REMOTE_USER = "root"  // 원격 서버 사용자    
        REMOTE_TOMCAT_DIR = "/usr/local/tomcat/webapps-javaee"  // Tomcat의 webapps 디렉터리
        REMOTE_TOMCAT_BIN = "/usr/local/tomcat/bin/"  // Tomcat의 bin 디렉터리
        BUILD_FILE_API = "api-server/build/libs/api-server.war"  // API 서버 WAR 파일 경로
        BUILD_FILE_FRONT_LGT = "lgu-front/build/libs/ROOT.war"  // Frontend WAR 파일 경로
        BUILD_FILE_FRONT_SKT = "skt-front/build/libs/ROOT.war"  // Frontend WAR 파일 경로
    }
    stages {
        stage('Print Parameters') {
            steps {
                script {
                    echo "Branch: ${params.branch}"
                    echo "Name: ${params.name}"
                    echo "Tag: ${params.tag}"
                    echo "Release IP: ${params.releaseip}"
                    echo "fronttype: ${params.fronttype}"
                }
            }
        }
        stage('Clone Repository') {
            steps {
                sh """
                    rm -rf cloned_repo || true
                    git clone --branch ${params.branch} ${REPO_URL} cloned_repo
                """
                echo "Git 저장소의 '${params.branch}' 브랜치가 성공적으로 클론되었습니다."
            }
        }
        stage('Build API Server') {
            steps {
                dir('cloned_repo') {  // 클론된 디렉터리에서 작업 수행
                    sh './gradlew :api-server:bootWar -Pprofile=stage'
                }
                echo "API Server 빌드 완료"
            }
        }
        stage('Build Front') {
            steps {
                script {
                    def frontBuildFile = ""
                    if (params.fronttype == "LGT") {  // LGT Frontend 빌드 선택
                        frontBuildFile = "${env.BUILD_FILE_FRONT_LGT}"
                        dir('cloned_repo') {  
                            sh './gradlew :lgu-front:bootWar -Pprofile=stage'
                        }
                        echo "LGT Frontend 빌드 완료"
                    } else if (params.fronttype == "SKT") {  // SKT Frontend 빌드 선택
                        frontBuildFile = "${env.BUILD_FILE_FRONT_SKT}"
                        dir('cloned_repo') {  
                            sh './gradlew :skt-front:bootWar -Pprofile=stage'
                        }
                        echo "SKT Frontend 빌드 완료"
                    } else {
                        error "Invalid fronttype parameter. Please specify either 'LGT' or 'SKT'."
                    }

                    env.FRONT_BUILD_FILE = frontBuildFile  // 선택된 WAR 파일 경로를 환경 변수에 저장
                }
            }
        }
        stage('Deploy to Remote Server') {
            parallel {
                stage('Deploy API Server') {
                    steps {
                        script {
                            def warFileApi = "${env.WORKSPACE}/cloned_repo/${BUILD_FILE_API}"
                            if (fileExists(warFileApi)) {
                                sh """
                                    scp ${warFileApi} ${REMOTE_USER}@${params.releaseip}:${REMOTE_TOMCAT_DIR}/api-server.war
                                """
                                echo "API Server WAR 파일이 원격 Tomcat에 배포되었습니다."
                            } else {
                                error "API Server WAR 파일이 존재하지 않습니다: ${warFileApi}"
                            }
                        }
                    }
                }
                 stage('Deploy Front') {
                    steps {
                        script {
                            def warFileFront = "${env.WORKSPACE}/cloned_repo/${env.FRONT_BUILD_FILE}"  // 선택된 프론트엔드 WAR 파일 경로 사용

                            if (fileExists(warFileFront)) {
                                sh """
                                    scp ${warFileFront} ${REMOTE_USER}@${params.releaseip}:${REMOTE_TOMCAT_DIR}/ROOT.war
                                """
                                echo "Frontend WAR 파일이 원격 Tomcat에 배포되었습니다."
                            } else {
                                error "Frontend WAR 파일이 존재하지 않습니다: ${warFileFront}"
                            }
                        }
                    }
                }
            }
        }
        stage('Restart Remote Tomcat') {
            steps {
                script {
                    try {
                        sh """
                            ssh -T ${REMOTE_USER}@${params.releaseip} "
                            cd ${REMOTE_TOMCAT_BIN} &&
                            ./shutdown.sh || echo 'Tomcat is not running or shutdown failed.' &&
                            sleep 5 &&
                            ./startup.sh || echo 'Tomcat startup failed.'
                            "
                        """
                        echo "원격 Tomcat 서버가 재구동되었습니다."
                    } catch (Exception e) {
                        error "Tomcat 재구동 중 오류 발생: ${e.message}"
                    }
                }
            }
        }
    }
}
```

### **2.4 사용 가이드라인**

#### **IntelliJ IDEA에서 커밋 메시지 작성 및 태그 추가**

##### **커밋 메시지 작성**

예시:

```plaintext
Commit Message: Commit 테스트입니다. @releaseip:'122.49.74.102' @fronttype:'LGT'
```

##### **태그 추가 및 Push**

IntelliJ IDEA에서 태그를 추가하고 Push하는 방법:

1. IntelliJ IDEA 상단 메뉴에서 `Git > Repository > Tag` 선택.
2. 새로운 태그 이름 입력 (예: `v1.0.0`).
3. 태그 생성 후 Push:
   
   ```bash
   git push origin v1.0.0
   ```

##### **결과 확인**

Push 후, Git Hook이 실행되어 다음 JSON 데이터가 Webhook으로 전송됩니다:

```json
{
    "branch": "chatbot_jenkins",
    "name": "강현우",
    "tag": "v1.0.0",
    "releaseip": "122.49.74.102",
    "fronttype": "LGT"
}
```

로그 파일(`/opt/git_pushlog/<브랜치 이름>/post-receive.log`)에서도 결과를 확인할 수 있습니다.


성공한 실제 로그파일 

```
[2025-01-17 14:05:00] JSON_DATA: {"branch": "  chatbot_jenkins", "name": "강현우", "tag": "jenkins_test_v0.2", "releaseip": "122.49.74.102", "fronttype": "LGT"}
  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                 Dload  Upload   Total   Spent    Left  Speed
100   423  100   293  100   130  48001  21297 --:--:-- --:--:-- --:--:-- 58600
{"jobs":{"eicn_dev":{"regexpFilterExpression":"","triggered":true,"resolvedVariables":{"branch":"  chatbot_jenkins","fronttype":"LGT","name":"강현우","releaseip":"122.49.74.102","tag":"jenkins_test_v0.2"},"regexpFilterText":"","id":109,"url":"queue/item/109/"}},"message":"Triggered jobs."}[2025-01-17 14:05:00] Webhook 호출 성공: 태그 jenkins_test_v0.2, 작성자 강현우, 브랜치 chatbot_jenkins, ReleaseIP 122.49.74.102, FrontType LGT

```



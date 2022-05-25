
#include<stdio.h>
#include<stdlib.h>
#include<arpa/inet.h>
#include<sys/socket.h>
#include<unistd.h>
#include<string.h>

int main(int argc, char* argv[])
{
        int serv_sock;
        int clint_sock;

        struct sockaddr_in serv_addr;
        struct sockaddr_in clint_addr;
        socklen_t clnt_addr_size;

        if (argc != 2)
        {
            printf("%s <port>\n", argv[0]);
            exit(1);
        }
        serv_sock = socket(PF_INET, SOCK_STREAM, 0); //1번
        if (serv_sock == -1)
            printf("socket error\n");

        memset(&serv_addr, 0, sizeof(serv_addr));
        serv_addr.sin_family = AF_INET;
        serv_addr.sin_addr.s_addr = htonl(INADDR_ANY);
        serv_addr.sin_port = htons(atoi(argv[1]));

        if (bind(serv_sock, (struct sockaddr*)&serv_addr, sizeof(serv_addr)) == -1) //2번
            printf("bind error\n");
        if (listen(serv_sock, 5) == -1) //3번
            printf("listen error\n");

        char st[] = "clos"; //잠금장치상태값(기본값="closed")

        while (1) {
            clnt_addr_size = sizeof(clint_addr);
            clint_sock = accept(serv_sock, (struct sockaddr*)&clint_addr, &clnt_addr_size);
            if (clint_sock == -1)
                printf("accept error\n");

            //서버코드추가 : 잠금장치 상태값을 st[]에 "open" or "clos" 저장

            char para[4];
            memset(para, 0, sizeof(para));
            int i = read(clint_sock, para, sizeof(para)); 
            para[4] = 0;

            if (strncmp(para, "home", 4) == 0) {
                printf("para명 : %s\n", para);
                memset(para, 0, sizeof(para));

                char state[4];
                memset(state, 0, sizeof(state));
                strcpy(state, st);
                printf("state: %s\n", state);

                memset(st, 0, sizeof(st));
                strcpy(st, state);

                write(clint_sock, st, sizeof(st));
            }

            if(strncmp(para, "open", 4) == 0) {
                printf("잠금장치상태 : %s\n", para);
                memset(st, 0, sizeof(st));
                strcpy(st, para);
                memset(para, 0, sizeof(para));

                //서버코드추가 : 카메라 사람수에 따른 진동여부 결정을 set에 "no" or "yes" 저장
                
                char set[] = "no"; //기본값 = "no"(사람수<=1)
                write(clint_sock, set, sizeof(set));
            }

            if (strncmp(para, "clos", 4) == 0) {
                printf("잠금장치상태 : %s\n", para);
                memset(st, 0, sizeof(st));
                strcpy(st, para);
                memset(para, 0, sizeof(para));
            }

            close(clint_sock);
        }
        close(serv_sock);
        
    return 0;
}

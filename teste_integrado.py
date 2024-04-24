import requests

def main():
    # Aguarda o input do accessToken
    accessToken = input("Aguardando accessToken: ")

    # Primeira chamada para o endpoint de login
    login_url = 'http://localhost:8090/api/auth/login'
    login_data = {'accessToken': accessToken}
    login_headers = {'Content-Type': 'application/json'}
    login_response = requests.post(login_url, json=login_data, headers=login_headers)

    # Verifica se o login foi bem-sucedido
    if login_response.status_code == 201:
        print("1 - TESTE LOGIN OK")
        # Extrai o id_token da resposta
        token = login_response.json().get('token').get('id_token')

        # Segunda chamada para o endpoint de postagem
        post_url = 'http://localhost:8090/api/post'
        post_data = {'content': 'TESTE AUTOMATIZADO'}
        post_headers = {
            'Authorization': f'Bearer {token}',
            'Content-Type': 'application/json'
        }
        post_response = requests.post(post_url, json=post_data, headers=post_headers)

        # Verifica se a postagem foi bem-sucedida
        if post_response.status_code == 201:
            print("2 - TESTE POSTAGEM OK")

            # Chamada GET para recuperar as postagens
            get_url = f'http://localhost:8090/api/post?page=0&size=10'
            get_headers = {'Authorization': f'Bearer {token}'}
            get_response = requests.get(get_url, headers=get_headers)

            # Verifica se a recuperação das postagens foi bem-sucedida
            if get_response.status_code == 200:
                print("3 - TESTE GET POSTAGENS OK")
                post_id = get_response.json()['content'][0]['id']  # Obtém o ID do primeiro post

                # Chamadas para teste de likes
                like_url = 'http://localhost:8090/api/like'
                like_types = ['LIKE', 'DISLIKE', 'LIKE']
                for likeType in like_types:
                    like_data = {
                        'likeEnum': likeType,
                        'post': {'id': post_id}
                    }
                    like_response = requests.post(like_url, json=like_data, headers=post_headers)

                if like_response.status_code == 201:
                    print("4 - TESTE LIKES OK")
                else:
                    print(f"Erro no teste de likes: {like_response.status_code}")
            else:
                print(f"Erro ao recuperar postagens: {get_response.status_code}")
        else:
            print(f"Erro na postagem: {post_response.status_code}")
    else:
        print(f"Erro no login: {login_response.status_code}")

if __name__ == "__main__":
    main()

import requests

def main():
    # Aguarda o input do accessToken
    code = input("Insira o authCode do Google: ")
    code = code.strip("'\"")
    
    # Primeira chamada para o endpoint de login
    login_url = 'http://localhost:8090/api/auth/login'
    login_data = {'code': code}
    login_headers = {'Content-Type': 'application/json'}
    login_response = requests.post(login_url, json=login_data, headers=login_headers)

    # Verifica se o login foi bem-sucedido
    if login_response.status_code == 201:
        print("01 - TESTE LOGIN OK")
        # Extrai o id_token da resposta
        token = login_response.json().get('idToken')

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
            print("02 - TESTE POSTAGEM OK")

            # Chamada GET para recuperar as postagens
            get_url = f'http://localhost:8090/api/post?page=0&size=10'
            get_headers = {'Authorization': f'Bearer {token}'}
            get_response = requests.get(get_url, headers=get_headers)

            # Verifica se a recuperação das postagens foi bem-sucedida
            if get_response.status_code == 200:
                print("03 - TESTE GET POSTAGENS OK")
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
                    print("04 - TESTE LIKES OK")

                    # Teste para criação de um comentário
                    comment_url = 'http://localhost:8090/api/comment'
                    comment_data = {'content': 'Comentário teste automático', 'post': {'id': post_id}}
                    comment_response = requests.post(comment_url, json=comment_data, headers=post_headers)
                    if comment_response.status_code == 201:
                        print("05 - TESTE CRIAR COMENTARIO OK")

                        # Teste de recuperação do comentário
                        get_comment_url = f'http://localhost:8090/api/comment/post/{post_id}?page=0&size=10'
                        get_comment_response = requests.get(get_comment_url, headers=post_headers)
                        if get_comment_response.status_code == 200:
                            print("06 - TESTE GET COMENTARIO OK")
                            
                            
                            comments_data = get_comment_response.json()
                            if comments_data['content']:
                                comment_id = comments_data['content'][0]['id']

                            # Testes de like, dislike e like no comentário
                            like_comment_url = 'http://localhost:8090/api/likecom'
                            for likeType in like_types:
                                like_comment_data = {'likeEnum': likeType, 'comment': {'id': comment_id}}
                                like_comment_response = requests.post(like_comment_url, json=like_comment_data, headers=post_headers)

                            if like_comment_response.status_code == 201:
                                print("07 - TESTE LIKES COMENTARIOS OK")
                                
                                
                                comment_reply_data = {'content': 'Comentário réplica teste automático', 'post': {'id': post_id}, 'parentComment': {'id': comment_id}}
                                comment_reply_response = requests.post(comment_url, json=comment_reply_data, headers=post_headers)
                                if comment_reply_response.status_code == 201:
                                    print("08 - TESTE CRIAR REPLICA OK")
                                    
                        
                                    get_comment_reply_url = f'http://localhost:8090/api/comment/son/{comment_id}?page=0&size=10'
                                    get_comment_reply_response = requests.get(get_comment_reply_url, headers=post_headers)
                                    if get_comment_reply_response.status_code == 200:
                                        print("09 - TESTE GET REPLICA OK")
                                        
                                        
                                        comments_reply_data = get_comment_reply_response.json()
                                        if comments_reply_data['content']:
                                            comment_reply_id = comments_reply_data['content'][0]['id']
                                        for likeType in like_types:
                                            like_comment_data = {'likeEnum': likeType, 'comment': {'id': comment_reply_id}}
                                            like_comment_reply_response = requests.post(like_comment_url, json=like_comment_data, headers=post_headers)
                                        if like_comment_reply_response.status_code == 201:
                                            print("10 - TESTE LIKES REPLICA OK")
                                            
                                            
                                            patch_comment_reply_url = f'http://localhost:8090/api/comment/{comment_reply_id}'
                                            patch_comment_reply_data = {'content': 'Comentário réplica teste automático EDITADO', 'post': {'id': post_id}}
                                            patch_comment_reply_response = requests.patch(patch_comment_reply_url, json=patch_comment_reply_data, headers=post_headers)
                                            
                                            if patch_comment_reply_response.status_code == 200:
                                                print("11 - TESTE EDITAR REPLICA OK")
                                                
                                                
                                                patch_comment_url = f'http://localhost:8090/api/comment/{comment_id}'
                                                patch_comment_data = {'content': 'Comentário teste automático EDITADO', 'post': {'id': post_id}}
                                                patch_comment_response = requests.patch(patch_comment_url, json=patch_comment_data, headers=post_headers)
                                                
                                                if patch_comment_response.status_code == 200:
                                                    print("12 - TESTE EDITAR COMENTÁRIO OK")
                                                    
                                                    
                                                    patch_post_url = f'http://localhost:8090/api/post/{post_id}'
                                                    patch_post_data = {'content': 'TESTE AUTOMATIZADO EDITADO'}
                                                    patch_post_response = requests.patch(patch_post_url, json=patch_post_data, headers=post_headers)
                                                    
                                                    if patch_post_response.status_code == 200:
                                                        print("13 - TESTE EDITAR POSTAGEM OK")
                                                        
                                                        delete_comment_reply_url = f'http://localhost:8090/api/comment/{comment_reply_id}'
                                                        delete_comment_reply_response = requests.delete(delete_comment_reply_url, headers=post_headers)
                                                        
                                                        if delete_comment_reply_response.status_code == 204:
                                                            print("14 - TESTE DELETAR REPLICA OK")
                                                            
                                                            
                                                            delete_comment_url = f'http://localhost:8090/api/comment/{comment_id}'
                                                            delete_comment_response = requests.delete(delete_comment_url, headers=post_headers)
                                                            
                                                            if delete_comment_response.status_code == 204:
                                                                print("15 - TESTE DELETAR COMENTÁRIO OK")
                                                                
                                                                delete_post_url = f'http://localhost:8090/api/post/{post_id}'
                                                                delete_post_response = requests.delete(delete_post_url, headers=post_headers)
                                                                
                                                                if delete_post_response.status_code == 204:
                                                                    print("16 - TESTE DELETAR POSTAGEM OK")
                                                                
                                                                
                                                                else:
                                                                    print(f"Erro ao deletar postagem: {delete_post_response.status_code}")                                                                
                                                            else:
                                                                print(f"Erro ao deletar comentário: {delete_comment_response.status_code}")
                                                        else:
                                                            print(f"Erro ao deletar replica: {delete_comment_reply_response.status_code}")
                                                    else:
                                                        print(f"Erro ao editar postagem: {patch_post_response.status_code}")
                                                else:
                                                    print(f"Erro ao editar comentário: {patch_comment_response.status_code}")
                                            else:
                                                print(f"Erro ao editar replica: {patch_comment_reply_response.status_code}")
                                        else:
                                            print(f"Erro no teste de likes da replica: {like_comment_reply_response.status_code}")
                                    else:
                                        print(f"Erro ao recuperar replicas: {get_comment_reply_response.status_code}")
                                else:
                                    print(f"Erro no teste de criar replica: {comment_reply_response.status_code}")
                            else:
                                print(f"Erro no teste de likes do comentário: {like_comment_response.status_code}")
                        else:
                            print(f"Erro ao recuperar comentário: {get_comment_response.status_code}")
                    else:
                        print(f"Erro ao criar comentário: {comment_response.status_code}")
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

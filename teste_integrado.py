import requests

class TestException(Exception):
    pass

def get_token():
    token = input("Insira o Bearer token: ").strip("'\"")
    return token

def log(message):
    print(message)

def post_content(token):
    log("Iniciando teste de criação de postagem")
    post_url = 'http://localhost:8090/api/post'
    post_data = {'content': 'TESTE AUTOMATIZADO'}
    post_headers = {
        'Authorization': f'Bearer {token}',
        'Content-Type': 'application/json'
    }
    response = requests.post(post_url, json=post_data, headers=post_headers)
    if response.status_code != 201:
        log("Falha no teste de criação de postagem")
        raise TestException(f"Erro na postagem: {response.status_code}")
    log("Teste de criação de postagem realizado com sucesso")
    return response.json()['id']

def get_posts(token):
    log("Iniciando teste de recuperação de postagens")
    get_url = 'http://localhost:8090/api/post?page=0&size=10'
    get_headers = {'Authorization': f'Bearer {token}'}
    response = requests.get(get_url, headers=get_headers)
    if response.status_code != 200:
        log("Falha no teste de recuperação de postagens")
        raise TestException(f"Erro ao recuperar postagens: {response.status_code}")
    log("Teste de recuperação de postagens realizado com sucesso")

def like_post(token, post_id, like_types):
    log("Iniciando teste de likes na postagem")
    like_url = 'http://localhost:8090/api/like'
    like_headers = {
        'Authorization': f'Bearer {token}',
        'Content-Type': 'application/json'
    }
    for likeType in like_types:
        like_data = {'likeEnum': likeType, 'post': {'id': post_id}}
        response = requests.post(like_url, json=like_data, headers=like_headers)
        if response.status_code != 201:
            log("Falha no teste de likes na postagem")
            raise TestException(f"Erro no teste de likes: {response.status_code}")
    log("Teste de likes na postagem realizado com sucesso")

def comment_post(token, post_id, content):
    log("Iniciando teste de criação de comentário")
    comment_url = 'http://localhost:8090/api/comment'
    comment_data = {'content': content, 'post': {'id': post_id}}
    comment_headers = {
        'Authorization': f'Bearer {token}',
        'Content-Type': 'application/json'
    }
    response = requests.post(comment_url, json=comment_data, headers=comment_headers)
    if response.status_code != 201:
        log("Falha no teste de criação de comentário")
        raise TestException(f"Erro ao criar comentário: {response.status_code}")
    log("Teste de criação de comentário realizado com sucesso")
    return response.json()['id']

def get_comments(token, post_id):
    log("Iniciando teste de recuperação de comentários")
    get_comment_url = f'http://localhost:8090/api/comment/post/{post_id}?page=0&size=10'
    get_comment_headers = {'Authorization': f'Bearer {token}'}
    response = requests.get(get_comment_url, headers=get_comment_headers)
    if response.status_code != 200:
        log("Falha no teste de recuperação de comentários")
        raise TestException(f"Erro ao recuperar comentário: {response.status_code}")
    log("Teste de recuperação de comentários realizado com sucesso")
    return response.json()['content'][0]['id']

def like_comment(token, comment_id, like_types):
    log("Iniciando teste de likes no comentário")
    like_comment_url = 'http://localhost:8090/api/likecom'
    like_headers = {
        'Authorization': f'Bearer {token}',
        'Content-Type': 'application/json'
    }
    for likeType in like_types:
        like_comment_data = {'likeEnum': likeType, 'comment': {'id': comment_id}}
        response = requests.post(like_comment_url, json=like_comment_data, headers=like_headers)
        if response.status_code != 201:
            log("Falha no teste de likes no comentário")
            raise TestException(f"Erro no teste de likes do comentário: {response.status_code}")
    log("Teste de likes no comentário realizado com sucesso")

def reply_comment(token, post_id, comment_id, content):
    log("Iniciando teste de criação de réplica de comentário")
    reply_comment_url = 'http://localhost:8090/api/comment'
    reply_comment_data = {'content': content, 'post': {'id': post_id}, 'parentComment': {'id': comment_id}}
    reply_comment_headers = {
        'Authorization': f'Bearer {token}',
        'Content-Type': 'application/json'
    }
    response = requests.post(reply_comment_url, json=reply_comment_data, headers=reply_comment_headers)
    if response.status_code != 201:
        log("Falha no teste de criação de réplica de comentário")
        raise TestException(f"Erro no teste de criar réplica: {response.status_code}")
    log("Teste de criação de réplica de comentário realizado com sucesso")
    return response.json()['id']

def get_replies(token, comment_id):
    log("Iniciando teste de recuperação de réplicas de comentário")
    get_comment_reply_url = f'http://localhost:8090/api/comment/son/{comment_id}?page=0&size=10'
    get_comment_reply_headers = {'Authorization': f'Bearer {token}'}
    response = requests.get(get_comment_reply_url, headers=get_comment_reply_headers)
    if response.status_code != 200:
        log("Falha no teste de recuperação de réplicas de comentário")
        raise TestException(f"Erro ao recuperar réplicas: {response.status_code}")
    log("Teste de recuperação de réplicas de comentário realizado com sucesso")
    return response.json()['content'][0]['id']

def patch_content(token, url, content):
    log(f"Iniciando teste de edição de conteúdo em {url}")
    patch_headers = {
        'Authorization': f'Bearer {token}',
        'Content-Type': 'application/json'
    }
    response = requests.patch(url, json={'content': content}, headers=patch_headers)
    if response.status_code != 200:
        log(f"Falha no teste de edição de conteúdo em {url}")
        raise TestException(f"Erro ao editar conteúdo: {response.status_code}")
    log(f"Teste de edição de conteúdo em {url} realizado com sucesso")

def delete_content(token, url):
    log(f"Iniciando teste de exclusão de conteúdo em {url}")
    delete_headers = {'Authorization': f'Bearer {token}'}
    response = requests.delete(url, headers=delete_headers)
    if response.status_code != 204:
        log(f"Falha no teste de exclusão de conteúdo em {url}")
        raise TestException(f"Erro ao deletar conteúdo: {response.status_code}")
    log(f"Teste de exclusão de conteúdo em {url} realizado com sucesso")

def main():
    token = get_token()

    post_id = post_content(token)

    get_posts(token)

    like_post(token, post_id, ['LIKE', 'DISLIKE', 'LIKE'])

    comment_id = comment_post(token, post_id, 'Comentário teste automático')

    get_comments(token, post_id)

    like_comment(token, comment_id, ['LIKE', 'DISLIKE', 'LIKE'])

    reply_comment_id = reply_comment(token, post_id, comment_id, 'Comentário réplica teste automático')

    get_replies(token, comment_id)

    like_comment(token, reply_comment_id, ['LIKE', 'DISLIKE', 'LIKE'])

    patch_content(token, f'http://localhost:8090/api/comment/{reply_comment_id}', 'Comentário réplica teste automático EDITADO')
    patch_content(token, f'http://localhost:8090/api/comment/{comment_id}', 'Comentário teste automático EDITADO')
    patch_content(token, f'http://localhost:8090/api/post/{post_id}', 'TESTE AUTOMATIZADO EDITADO')

    delete_content(token, f'http://localhost:8090/api/comment/{reply_comment_id}')
    delete_content(token, f'http://localhost:8090/api/comment/{comment_id}')
    delete_content(token, f'http://localhost:8090/api/post/{post_id}')

if __name__ == "__main__":
    try:
        main()
    except TestException as e:
        print(e)

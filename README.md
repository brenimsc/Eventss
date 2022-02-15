# Aplicativo Events

<p align="center" > Esse projeto foi desenvolvido mediante um desafio Sicredi. Nele foram utilizadas algumas ferramentas do android Jetpack como LiveData, Room, Material Design Components, e etc. O Aplicativo tem suporte a partir da API 19 até a mais recente. <p>

### Features

+ O app exibe uma lista de eventos, que são coletados por meio de uma api com os dados.
+ Possui a funcionalidade de clicar em algum evento específico e ver mais informações sobre
+ O usuario tem a possibilidade de fazer Check-in em algum evento desejado e que posteriormente ficará uma lista salva internamente de todos os eventos que foi feito o checkin
+ Para realizar check_in o usuário deve enviar seu nome e email, que ficam salvos internamente também para que não tenha que digitar todas as vezes.
+ Funcionalidade de marcar eventos como favoritos, e também possui a lista de todos os favoritos
+ Tanto a lista de favoritos como a lista de Checkins são salvas internamente
+ É possível compartilhar o evento

![](https://github.com/brenimsc/Events/blob/master/images/mvvm.png)


### Desenvolvimento

O App utiliza a arquitetura MVVM, que no caso é a arquitetura recomendada pelo Google. Com essa arquitetura podemos realizar com bastente eficácia a separação das classes de lógica com a interface do usuário UI. Provendo assim, uma separação de responsabilidades.


### Principais tecnologias utilizadas

+ Retrofit - Utilizado para realizar a comunicação externa do app, como por exemplo realizar as requisições na API.
+ Koin - Injeção de dependência, delega a responsabilidade de iniciar as dependências, permitindo que membros apenas peçam o que precisam e a instância é fornecida automaticamente. Dessa forma a separação de responsabilidades do projeto ficam mais eficaz ainda.
+ Coroutines - Utilizado para realizar as operações assincronas, como por exemplo acessar nossa api. Fazendo com que assim não bloqueie a Thread principal enquanto realiza as ações. Realiza funções de suspensões, que são retomadas posteriormente em alguma tela. Alem da implementação ser simples.
+ Room - Para salvar os dados internamente, que se beneficia de uma abstração sobre o SQLITE. Além de que em conjunto com o LiveData tem um beneficio muito legal de Observer.
+ Glide - Utilizado para carregar e auxiliar a exibir as imagens que possuem um endereço de URL.

## Preview

![](https://github.com/brenimsc/Events/blob/master/images/events_inicial.gif)
![](https://github.com/brenimsc/Events/blob/master/images/events_meio.gif)
![](https://github.com/brenimsc/Events/blob/master/images/events_final.gif)


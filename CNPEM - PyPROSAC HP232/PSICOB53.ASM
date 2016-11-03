$pg pl=60
$pg pagewidth=131
$date

	; ATENCAO - AS MODIFICACOES PARA NOVAS VERSOES ESTAO NO FIM DESTE
	; COMENTARIO, ANTES DO INICIO DO PROGRAMA!!!
	;
	;
	; PSICO PROGRAMA DO SISTEMA DE CONTROLE

	; LNLS - CONTROLE - OUTUBRO/95 (00)
	; lnln - controle - janeiro/96 (01) - James R. Piton / Guilherme Franco
	; lnls - controle - janeiro/96 (XX) - James R. Piton / Guilherme Franco
	; lnls - controle - janeiro/96 (YY) - James R. Piton / Guilherme Franco
	; lnls - controle - fevereiro/96 (ZZ) - James R. Piton / Guilherme Franco
	; lnls - controle - marco/96 (A0-a1) - James R. Piton / Guilherme Franco
	; lnls - controle - abril/96 (A2-A4) - James R. Piton / Guilherme Franco
	; lnls - controle - maio/96 (A5-A9) - James R. Piton / Guilherme Franco
	;
	; -Evandro Avancini
	; -Sheila Pinto
	; -Flavio Silva
	; -Anderson Pissetti
	; -Glauber Monteiro
	; -Alexandre Nomura
	; -James Piton
	; -Guilherme Franco
	; - e mais alguns que ja se mandaram (Samuel Yamaki, Marcio Silva,
	;	Patricia, Simone, Sergio, Marcelo, Germano, etc) !!
	;
	; Este program esta sendo desenvolvido para gerenciamento do "MALUCO"
	; O sistema "LOCO" nao suporta este programa a nivel de hardware
	; O modulo $F, offset $8 esta reservado pela placa de CPU
	; Nao utilizar interface neste modulo!!
	; Memoria Eprom: $0000 a $7fff	(programa permanente)
	; Banco 0 ram:	$8000 a $9fff	(banco partilhado por paginacao)
	; Banco 1 ram:	$a000 a $bfff	(rascunho e memoria auxiliar)
	; Banco 2 ram:	$c000 a $dfff	(rascunho e memoria auxiliar)
	; Banco 3 ram:	$e000 a $ffff	(pilha)
	; versao 01 -
	; nao leva em conta possiveis colisoes na DPM!
	; possui a possibilidade da instalacao de uma rotina especial de ate
	; 8 Kbytes na regiao de paginacao da ram, instalada por meio de uma
	; placa serial especial (rs232)!
	; esta placa serial tambem permite a espionagem das operacoes da cpu!
	; necessario a instalacao de um opcode RET na posicao $8000 durante o boot!
	; utiliza alocacao dinamica de memoria para I/O (locon 3B, locomux 9B, etc.)
	;
	; versao xx -
	; esta versao esta sendo escrita para teste no linac, e nao possui
	; as subrotinas para todas as placas.
	; placas nao implementadas (versao 02 ja possui algumas):
	; SERVACUO, CONTFOTO, DAC_CORR, LOQUADRA, MOT_PAS1, MOT_PAS2,
	; BPM_X, BPM_Y, STAT_FNT e SERRS232.
	;
	; versao yy - primeira versao completa

	; versao zz - novos comandos: boot remoto, carregamento das configuracoes
	; 23/02	- corrigido erro na leitura da locomux
	; 26/02	- apos o disparo de um conversor o wdt nao e' refrescado, para
	;		evitar-se que a retirada de uma placa implique no travamento
	;		da CPU. Se uma placa e' retirada, o reboot sera' imediato.
	;		- display e' alterado para indicar a situacao em que se encontra
	;		a CPU (em boot remoto, enviando a configuracao etc.)
	; 28/02	- string de identificacao de eprom (numa linha apos o reset)
	;		- criadas as placas RUX12BMP e RUX12BBP (Locomux rapida, com menor
	;		delay entre as placas)

	; versao A0 - criadas as rotinas para SERVACUO (04/03)
	;		- eliminadas as rotinas para SERVACUO (13/03)
	;		- criada STAT_FNT (19/03)

	; versao A1 - criadas as rotinas para BPM_X e BPM_Y (25/03)

	; versao A2 - revista a rotina para DIGIINT (faltava RET ao final do
	;		codigo da rotina digiint1 (22/04)

	; versao A3 - corrigido erro na escrita da placa 16 bits
	;		- especificado o tratamento para o modo 1 de interrupcao
	;		- execucao de rampa (24/04)

	; versao A4 - modificado o sistema de leitura do monitor de posicao,
	;		com a criacao da placa BPM_P (29/04)

	; versao A5 - Experimento com a eliminacao do tempo de chaveamento entre
	;		canais da RUX (02/05) e BPM_P

	; versao A6 - Corrigida a falta de ajuste inicial (apos o boot) dos
	;		valores default (06/05)

	; versao A7 - Permite que a leitura das placas BPM_P seja ativada ou
	;		desativada apenas pela selecao da prioridade (08/05).

	; versao A8 - Tempo para selecao de canal de placas do tipo LOCOMUX volta
	;		a existir, porem valendo 100 us (13/05)

	; versao A9 - modificados os codigos de comando (o que torna as versoes ante-
	;		riores incompativeis
	;		- quando no estado de identificacao de comando, sinaliza com 2
	;		no primeiro byte (23/05)

	; versao B0 - corrigida a rotina de rampeamento	(5-10/06)
	;		- adicionado um delay de 4 ms para o chaveamento da placa BPM_X (12/05)

	; versao B1 - criada a placa contadora (13/05)

	; versao B2 - criado um buffer especial para transferencia de rampa
	;		- indicador de transferencia de blocos de rampa no display
	;		- leitura de configuracao e' interrompida tambem se o comando for
	;		de ajuste
	;		- criada a funcao de contagem de reset, para o comando de confi-
	;		guracao
	;		- criado o comando de travamento e destravamento de atualizacao
	;		de leituras, para maior rapidez na transmissao de rampa
	;		- bloco de dados na transmissao de rampa passa de 128 a 240 bytes
	;		(27/06) e (01/07)

	; versao BG - (testes) delay para chaveamento da placa BPM_X foi aumentado
	;		para 100 ms e ficou em definitivo (15/07)

	; versao B3 - espaco para buffer de rampas era insuficiente depois que o ta-
	;		manho dos blocos passaram a 240 bytes por transmissao. A solucao
	;		foi aumentar o bloco de 16384 para 17000 bytes (25/07)

	; versao B4 - deixa de existir a placa LOCOMUX (8/11), ficando apenas as RUX

	; versao B5 - valor default dos flags passou a ser $80
	;		criado novo tipo de placa relogio 16bits(RELOG16), tipo 25
	;		implementado programa de ciclagem das fontes(12/97)

	;#################### Alexandre

	; versao B6 - implementado sistema de interpolacao/4 entre dois pontos da rampa (11/03/98)
	; versao B7 - mudanca da sequencia no procedimento de ajuste da lcn16bmp e lcn16bbp
	;		essas mudancas tambem foram realizadas no procedimento de ajuste lcn16b
	;		da ciclagem, dos passos e subpassos da rampa (17/03/98).
	; versao B8 - como RELOG16, tipo 25 criado na versao B5 nunca foi usado pelo Psico
	;		entao, a nova placa criada: placa serial RS-232/RS-422/RS-485 (SER232) sera
	;		tipo 25.
	;		criado novo tipo de placa serial (SER232), tipo 25(especifico para leitura
	;		do meter HP34401A)	em	05/08/98
	;
	; versao B9 - Implementacao de mudança no tratamento de interrupcao para rampeamento de
	;		placas LCN16b. Essa mudança visa atender a necessidade de termos LCN16b com
	;		pulsos entre passos igual a 1 e também igual a 4	(24/11/98) .
	;
	; versao B10 - Criado CMD_AJUSTE2($08) que carrega a word nos DAC's mas nao dispara a conversão
	;		A conversão é disparada pela trata_int3($77) que também foi criada em "VB10"
	;		Também foi criado o buffer síncrono que é usado para mascarar o disparo dos
	;		DAC's quando é utilizado o CMD_AJUSTE2.	(02/12/98)
	;
	;versao	B11 - Criado novo tipo de placa(tipo 26 -> CDLCNADD) e rotinas correspondentes.
	;		Esta é uma placa de 12 bits que entrega a soma de 16 leituras; é uma
	;		pseudo 16bits, mas apenas na leitura. A escrita analogica continua sendo
	;		em 12bits e as rotinas de leitura/escrita digitais permanecem inalteradas.
	;		(11/01/1999)
	;
	;versao B12	- Criado novo tipo de placa (tipo 15 -> CDACIOTRAN) e rotinas correspondentes.
	;		Esta é uma placa de acionamento por transistor e trabalha com logica invertida
	;		(01/02/1999)
	;
	;versao B13	- Redefinido o tipo de placa para a CDACIOTRAN.
	;		De tipo 15 para tipo 27. (19/04/1999)
	;
	;versao B14	- Correcao na subrotina placa_16a para ciclagem;
	;		- Mudanca na leitura da placa lcn16bits; disparo duas leituras
	;		pois o novo conversor(AD977BN) devolve na forma serial o resultado
	;		da conversao anterior.
	;
	;versao B15	- Criado novo tipo de placa (UDC3000 - tipo 28). Esta e uma placa serial RS485
	;		para leitura e escrita dos controladores de temperatura UDC3000 da Honeywell.
	;		Se esta placa estiver no bastidor, o numero de ciclos para inversao do ponto no
	;		display sera de 1 ciclo.
	;		( 04/10/1999 )
	;
	;versao B16	- Criado novo tipo de placa (LCN16b32MM - tipo 29 ). Esta e uma placa locon
	;		16bits versao 3.2 com transformacao	da leitura bipolar em monopolar.
	;		( 19/10/1999 )
	;
	;versao B17	- Criado novo tipo de placa (mAtoV - tipo 30 ). Esta placa possui 8 canais de
	;		conversao de sinal de entrada de 4-20mA para 0-10V em formato digital com
	;		resolucao de 12bits. ( 13/12/1999 )
	;
	;versao B18	- Correcao na rotina de habilitacao de rampa para que os dados enviados na
	;		transmissao de rampa fossem reaproveitados apos o final ou o aborto
	;		de uma rampa anterior. A correcao consistiu em zerar o flag de interpolacao
	;		no mapa de rampa de cada placa rampeavel. (11/01/2000)
	;
	;versao B19	- Implementacao de time-out's distintos para placa UDC3000 e placa SER232
	;		- Correcao da posicao inicial da string com a marca da data e versao do
	;		programa gravado na EPROM. (20/01/2000)
	;
	;versao B20	- Incluido novo tipo de ciclagem (tipo23). (07/02/2000) Alexandre Nomura


	;#################### James (Fevereiro/2000)

	;versao B21	- Revisao com vistas ao Sincrotron Injetor (29/05/2000) James Piton
	;
	;versao B22	- implementacoes do Sincrotron Injetor (14/08/2000)
	;
	;versao B23 e B24 - implementacoes do Sincrotron Injetor (25/08/2000) - James
	;		com comandos para BPM integrada 64 vezes
	;versao B25 e B26	- comandos especiais de BPM e deteccao de corrente comecam
	;		a ser transformados em parametros de ajuste das resp. placas
	;		(04/09/2000)
	;		- o numero de integracoes e' configuravel como parametro de ajuste
	;		(11/09/2000)
	;versao B27	- implementacoes da deteccao de corrente para inicio de injecao
	;		no booster (14/09)
	;		- implementacoes da placa contadora de fotons
	;		- otimizacao de codigo com supressao do excesso de "inc ix"
	;		e "inc iy" desnecessarios, passando a usar ld (ix+n)
	;		- rotinas UDC foram otimizadas (26/09)
	;		- rotinas CONTDCCT foram criadas (26/09)

	;versao B28	- (06/11) na placa LCN12ADD o metodo de escrita chama a escrita da
	;			locon convencional
	;versao B29	- (17/11) placa RELOGBOO passa a indicar um byte de estado do
	;		processo de rampa
	;		- invertida a ordem de escrita nos bytes da placas
	;		de ATRASBOO para que seja primeiro desligada e depois
	;		ajustados os bytes de atraso
	;		(20/11) efetivado o pulso da linha HEJE na ejecao

	;versao B29a	- (21/11) parametros de rampa so' sao acolhidos quando RELOGBOO
	;			recebe o parametro habilitar deteccao de corrente
	;		- boot de CONTDCCT e CONTBOO nao e' zero, para evitar interrupcoes parasitas

	;versao B29d	- (22/11) diversos pequenos detalhes

	;versao B29e	- (29/11) faltava um "cp" na rotina verif5
	;		- consertado o display no final da rampa do booster
	;versao B30	- (30/11) instalada em todas as fontes ciclaveis

	;versao B31	- (05/12) cpu passa a ter um bit de conf. de velocidade, lido
	;		no momento do boot para corrigir o calculo de tempo
	;versao B32	- (07/12) eliminada a redundancia de calculo dos substeps
	;		na execucao da rampa

	;versao B33	- (07/12) passo de ejecao nao representa o ultimo passo da rampa
	;versao B34	- (07/12) portanto uma vez ocorrida a ejecao, nao deve-se mais
	;			prestar atencao - ele recebe FFFF
	;			- soma 64, se ejeta ou
	;			- soma 128, se a rampa e' parada

	;versao B35	- (08-11/12) tentativa de se tratar a interrupcao de rampa sem
	;			perder a contagem de nenhum pulso
	;versao B36	- (12/12) pequena correcao na rampa para que as placas com skip = 4
	;			comecem o ajuste desde o primeiro pulso
	;			- revisao dos comandos que desbloqueiam respostas
	;			de comandos anteriores
	;versao B37	- (20/12) faz a coleta da BPM no modo rampa n~ao apos um dado
	;			numero de pulsos, mas de passos do dipolo, que leva
	;			em consideracao as placas com skip = 4
	;			- para evitar colisao, foi comentada a leitura
	;			(desnecessaria) de todo o bloco a cada ciclo
	;			- corrigida a verificacao de deteccao de corrente
	;
	;
	;############################## 2001 ########################################
	;
	;
	;versao B38 - (15/02/2001)
	;			fez-se coincidir o passo inicial da rampa 12 bits com
	;			a rampa de 16 bits
	;			alto nivel passa a respeitar a relacao
	;			Npu = 4 * Npa(16) - 3
	;			tal que Npu = 16 * Npa (12) - 15

	;versao B39 - (30/03/2001)
	;			acrescidas as curvas de ciclagem dos tipos 24 e 25 (Eprom C)

	;versao B40 - (03/05/2001)
	;			durante a rampa, o modulo RELOGBOO nao aceita ajustes que
	;			venham a ser feitos no bastidor de sincronismo
	;versao B41 - (23/05/2001)
	;			incluido o tratamento basico da placa MUX16BBP (microcontrolada), tipo 17
	;			implementado o tratamento da MUX16BBP (microcontrolada), tipo 17
	;			testes realizados (07/06/2001)
	;versao B42 - (07/08/2001)
	;			a verificacao se o no. de intervalos 'e zero faz
	;			a propria placa
	;versao B43 - (03/09/2001)
	;			passa a permitir que a MUX16BBP fixe o chaveamento
	;			em um canal especifico (modos de operacao 10h, 20h, 30h ou 40h
	;versao B44 - (05/09/2001)
	;			passa a tratar a rampa ciclica automatica, tanto no
	;			modulo de sincronismo como nas do booster:
	;			introduzido assim o parametro 127 na deteccao de
	;			corrente do relogboo (deteccao ciclica) e o comando
	;			208 (D0h) de habilitacao de rampa (ciclica) nas fontes
	;			criado wait50ms

	;versao B45 - (10/09/2001)
	;			erro no tamanho de bytes do relogboo
	;			nao estava desempilhando valores no retorno do
	;			tratamento da rampa ciclica (trata_int)

	;versao B45 - (18/09/2001)
	;			DIGBOO sempre tem tres portas de saida, por definicao
	;			e nao se olha mais para a config. da porta
	;versao B46 - (11/09/2002)
	;versao B47 - (13/09/2002)
	;			nova configuracao para o HP232 (resolucao MIN) e
	;			portanto foi necessario introduzir um tempo apos
	;			o comando de leitura

	;versao B48 - (20/09/2002)
	;versao B49 - (01/10/2002)
	;			corrigidos diversos problemas com a leitura HP232
	;			- eliminado o ineficiente controle DTR/RTS
	;			criada uma protecao (pende232) para que um comando de
	;			configuracao so' atinja o HP232 quando a leitura
	;			anterior tiver terminado

	;versao B50 - (28/11/2002)
	;			foi inicializada a variavel pende232 no boot tamb'em

	;versao B51 - (09/12/2002)
	;			corrigida a leitura HP 232, com handshake
	;		(12/12/2002)
	;			ainda havia problemas com a leitura e a
	;			pendencia de tratamento de escrita. Com uma
	;			combinacao de situacoes, implicava em
	;			esperar a chegada dos 17 caracteres, mesmo que
	;			o multimetro nao estivesse mais sinalizando a
	;			linha de envio. Isso levava a uma situacao em que a
	;			CPU nao solicitava mais leituras ao multimetro.

	;versao B52 - (13/04/2004)
	;			a placa contadora de fotons (CONTFOTO) ganhou um novo
	;			comando (192), indicando que ao terminar uma contagem
	;			por tempo seja redisparada automaticamente

	;versao B53 - (14/09/2004)
	;		criados os tipos L12_MAXMIN e L16_MAXMIN, para leitura
	;		com integração (número de leituras configurável) e 
	;		registro de máximo e mínima leitura

				defseg boot, start=$0000
				seg boot

	; definicao das constantes mais importantes
dsp_wdt				equ		$fe		;refresco wdt e escrita display
config				equ		$fe		;leitura configuracao do software
ram_int				equ		$ff		;leitura seleciona ram interna
ram_ext				equ		$ff		;escrita seleciona ram exter. (255)
sel_rom				equ		$fd		;leitura seleciona eprom
sel_ram				equ		$fd		;escrita seleciona ram
ret_esp				equ		$C9		;valor de return
tot_pas_mais			equ		$ff		;valor +sig num.pas.ciclagem
tot_pas_menos			equ		$ff		;valor -sig num.pas.ciclagem

	;Comandos Concentrador-No

CMD_NORMAL			equ		$00
CMD_AJUSTE			equ		$01
CMD_CONFIG			equ		$02
CMD_FIMCONFIG			equ		$03
CMD_BOOT			equ		$04

CMD_MSGRECEB			equ		$05		;concentrador ja' entendeu a mensagem
CMD_CONPLCIC			equ		$06		;comando de configuracao de placas para ciclagem
CMD_CCONFCIC			equ		$07		;comando de confirmacao para ciclagem
CMD_AJUSTE2			equ		$08		;comando de ajuste sem disparo do DAC nas LCN's 12b e/ou 16b
CMD_NRESET			equ		12		;contagem de resets, uso restrito ao
CMD_FIMNRESET			equ		13		;	grupo de controle

CMD_RSTRAMPA			equ		200		;Limpeza de rampa (deve anteceder o envio
				;		do primeiro bloco)
CMD_TXRAMPA			equ		201		;concentrador transmite bloco de rampa
CMD_SOLRAMPA			equ		202		;No solicita proximo bloco
CMD_HABRAMPA			equ		203		;concentrador habilita comeco de rampa
CMD_ABORAMPA			equ		204		;concentrador informa aborto de rampa
CMD_HLTRAMPA			equ		205		;No' confirma rampa abortada
CMD_FIMRAMPA			equ		206		;rampa completada
CMD_DESTRAVA			equ		207		;volta a fazer leituras
CMD_RAMPACIC			equ		208		;habilita rampa ciclica (fontes Booster)

CMD_HABCICLA			equ		224		;comando de habilitacao de ciclagem
CMD_ABOCICLA			equ		225		;comando de aborto de ciclagem
CMD_CICLABOR			equ		226		;comando de ciclagem abortada
CMD_CICLCOMP			equ		227		;comando de ciclagem completada
CMD_CICLINEX			equ		228		;comando de tipo de ciclagem inexistente
CMD_PLNAOCIC			equ		229		;comando de tipo de placa nao ciclavel

CR				equ		$0D
LF				equ		$0A

	;parametros para a interrupcao
INT_RAMPA			equ		$55
INT_AJUSTE2			equ		$77
INT_BPM				equ		$99
INT_CICLA			equ		$AA
INT_WAITDCCT			equ		$BB
INT_LEITDCCT			equ		$CC
INT_RAMPABOO			equ		$DD
INT_LEI232			equ		$EE

	;estado da contadora de fotons
FOT_ZERA			equ		0
FOT_PARA			equ		1
FOT_EMCONTAGEM			equ		255
FOT_FREERUN			equ		127
FOT_CONTINUO			equ		192


	;estado da contadora de fotons
LIGA_RELOG			equ		255
DESL_RELOG			equ		1

DOZE_BITS			equ		12
DEZESSEIS_BITS			equ		16

	;Operacoes para as subrotinas
OPR_LEITURA			equ		0 ;atualizar o bloco 0 da DPM com leituras
OPR_ESCRITA			equ		1 ;atualizar a placa com dados do bloco DPM 1
OPR_PROXIMA			equ		2 ;ir para a proxima posicao da placa nos blocos DPM
OPR_BOOT			equ		3 ;fazer a inicializacao da placa

	;Codigos de indentificacao de placa

CODSER2MBPS			equ		0
CODLCN12BMP			equ		1
CODLCN12BBP			equ		2
CODLCN16BMP			equ		3
CODLCN16BBP			equ		4
CODMUX12BMP			equ		5
CODMUX12BBP			equ		6
CODDIG24CFD			equ		7
CODDIG24CFO			equ		8
CODDIGIINT			equ		9
CODRELOGSIN			equ		10
CODATRASSIN			equ		11
CODCONTADOR			equ		12
CODCONTFOTO			equ		13
CODMUX16BBP			equ		17
CODBPMX				equ		18
CODBPMY				equ		19
CODSTATFNT			equ		20
CODRUX12BMP			equ		22
CODRUX12BBP			equ		23
CODBPMP				equ		24
CODSER232			equ		25
CODLCNADD			equ		26
CODACIOTRAN			equ		27
CODUDC3000			equ		28
CODLCN16B32MM			equ		29
CODMATOV			equ		30
CODRELOGBOO			equ		31
CODATRASBOO			equ		32
CODBPM64			equ		33
CODFOTODCCT			equ		34
CODFOTOBOO			equ		35
CODDIGBOO			equ		36
	;
	;
CODL12MAXMIN			equ		39
CODL16MAXMIN			equ		40

NAO_INSTALADA			equ		63
NAO_CICLAVEL			equ		128

DISPLAY_LIMPA			equ		%11111111
DISPLAY_C			equ		%11000110
DISPLAY_Cicl			equ		%10100111
DISPLAY_H			equ		%10001001
DISPLAY_U			equ		%11000001
DISPLAY_1			equ		%11111001
DISPLAY_2			equ		%00100100
DISPLAY_4			equ		%10011001
DISPLAY_Rmin			equ		%11010000
DISPLAY_Cmin			equ		%10100111
DISPLAY_Dmin			equ		%10100001
DISPLAY_SUBIDA			equ		%10101101		;mostra traco de subida, simbolo da rampa
DISPLAY_IGUAL			equ		%11110110		;indica que esta' em deteccao (sinal de igual)
DISPLAY_MENOS			equ		%10111111		;mostra sinal de menos
DISPLAY_TOPO			equ		%11111110		;mostra sinal de menos
DISPLAY_UNDERL			equ		%11110111		;mostra sinal de menos
DISPLAY_ESQINF			equ		%11101111
DISPLAY_DIRSUP			equ		%11111101

	;------ Status do RELBOO

STBOO_CORDESABILITADA		equ		20
STBOO_CORHABILITADA		equ		21
STBOO_RAMPAFAIXAS		equ		30
	; 30: 1a. veloc., 31: 2a. veloc. ... 39: 10a. veloc.
STBOO_EJETADO			equ		64
STBOO_RAMPACOMPLETA		equ		128

	;------------------------

	;Blocos da DPM
BLOCO_SAIDA			equ		0 ;Bloco em que a CPU escreve na DPM
BLOCO_ENTRADA			equ		1 ;Bloco que a CPU normalmente le

MAXINTEG12BITS			equ		1000  ;integracoes para a leitura L12MAXMIN
MAXINTEG16BITS			equ		256   ;integracoes para a leitura L16MAXMIN
MAXSERIESINTEG			equ		2



	; regiao de reset boot total

reset				ld		sp,$0000		;proxima chamada=topo ram $ffff
				im		1		;seta o modo 1 de interrupcao
				di		;ainda nao estou apto p/ int
				jp		execreset

	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;	Versao e data para identificacao de eprom (max. 13 caracteres)
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

marca_versao			DB		"13/04/04-B52 "

				org		$0038

	; regiao de interrupcao mascaravel

int				di
				jp		trata_masc

				org		$0066

	; regiao de interrupcao nao mascaravel

nmi				di
				retn


	; faz inicializacoes de boot

execreset			ld		hl, (contreset)
				inc		hl
				ld		(contreset), hl

				ld		a, DISPLAY_LIMPA
				ld		(display),a		;
				call		wdt		;aciono escrita display e wdt

				in		a, (config)
				cpl
				and		%00000011
				cp		0
				jp		nz, mhz8
				;------------ tempos para CPU com 4 MHz
				ld		a,20
				ld		(ciclos100us), a
				jp		velocdeterminada

				;------------ tempos para CPU com 8 MHz
mhz8				cp		1
				jp		nz, mhz16
				ld		a,40
				ld		(ciclos100us), a
				jp		velocdeterminada

				;------------ tempos para CPU com 16 MHz
mhz16				ld		a,80
				ld		(ciclos100us), a

velocdeterminada
				xor		a
				ld		(bastidorBPM), a
				ld		(sinalizador), a
				ld		(modorampaBPM), a
				ld		(sincrono), a
				ld		(interrupt), a
				ld		(em_deteccao), a
				ld		(rampa_ciclica), a
				ld		(display_faixa), a
				ld		(relogboo_cego), a
				ld		(pende232), a

				ld		de, 0
				ld		(tentou232), de
				ld		de, MAXTENTAT232 * 8
				ld		(tentativas232), de

				inc		a
				ld		(libera_leitura), a

				in		a,(ram_int)		;seleciono ram interna
				in		a,(sel_rom)		;seleciono eprom

				ld		a,ret_esp		;
				ld		(rot_esp),a		;preparo return da rotina especial

				ld		a, TOTCARAC232
				ld		(qtcarac232), a		;inicializa como nao havendo
				;mais caracteres a chegar
				jp		prog_pri		;programa principal


trata_masc			ex		af, af'
				ld		a,(interrupt)

				cp		INT_RAMPA
				jp		z, e_rampa		;faz o trat.int. da rampa

				cp		INT_RAMPABOO		;trat. int. para inspecionar
				jp		z, e_rampaboo		;		passo de rampa booster

				cp		INT_BPM
				jp		z, e_bpm		;faz o trat.int. das BPM

				cp		INT_WAITDCCT		;trat. int. para iniciar
				jp		z, e_waitdcct		;		a det. corrente

				cp		INT_LEITDCCT		;trat. int. para ler
				jp		z, e_leitdcct		;		dcct

				cp		INT_AJUSTE2
				jp		z,e_ajuste2		;faz o trat.int. para disparo do DAC
				;das LCN's que sofreram um pre-load
				;atraves do CMD_AJUSTE2
				cp		INT_CICLA
				jp		z,e_cicla		;faz o trat.int. da ciclagem

				cp		INT_LEI232
				jp		z,e_232

				reti		;nenhuma delas

e_rampa				;;; nao "destroca" AF e AF'		porque pode ser desnecessario
				;;; atender a execucao - veja trata_int
				jp		trata_int

e_232				ex		af, af'
				jp		trata_232

e_cicla				ex		af, af'
				jp		trata_int2

e_ajuste2			ex		af, af'
				jp		trata_int3

e_bpm				ex		af, af'
				jp		trata_bpm

e_leitdcct			ex		af, af'
				jp		trata_leitdcct

e_waitdcct			ex		af, af'
				jp		trata_waitdcct

e_rampaboo			ex		af, af'
				jp		trata_rampaboo

				org		$0150

	; regiao de tabelas de subrotinas

	; escreve no display caracter contido em display e refresca wdt

wdt:				push		af		;salvo a e flags
				ld		a,(display)		;
				out		(dsp_wdt),a		;escrevo e refresco wdt
				pop		af		;restauro a e flags
				ret

	;*************************************************
	; tabela com tipo de placas definidas
	;*************************************************

tab_desvio			dw		ser2mbps		;0
				dw		lcn12bmp		;1
				dw		lcn12bbp		;2
				dw		lcn16bmp		;3
				dw		lcn16bbp		;4
				dw		mux12bmp		;5
				dw		mux12bbp		;6
				dw		dig24cfd		;7
				dw		dig24cfo		;8
				dw		digiint		;9
				dw		relogsin		;10
				dw		atrassin		;11
				dw		contador		;12
				dw		contfoto		;13
				dw		nao_inst		;14
				dw		nao_inst		;15
				dw		nao_inst		;16
				dw		mux16bbp		;17		; mux 16 bits microcontrolada
				dw		bpm_x		;18
				dw		bpm_y		;19
				dw		stat_fnt		;20
				dw		nao_inst		;21
				dw		rux12bmp		;22
				dw		rux12bbp		;23
				dw		bpm_p		;24
				dw		ser232		;25
				dw		lcn12add		;26
				dw		aciotran		;27
				dw		udc3000		;28
				dw		lcn16b32mm		;29
				dw		matov		;30

				dw		relogboo		;31
				dw		atrasboo		;32
				dw		bpm64		;33
				dw		contdcct		;34
				dw		contboo		;35
				dw		digboo		;36

				dw		nao_inst		;37
				dw		nao_inst		;38

				dw		l12maxmin		;39
				dw		l16maxmin		;40

				dw		nao_inst		;61
				dw		nao_inst		;63
fimtab				db		0

NUM_PLACAS			equ		(fimtab-tab_desvio)/2		;numero de posicoes na tab_desvio

	; =============================================================
	; ==================	comandos para multimetro na serial RS232

STR232_RST			db		"*RST",CR,LF,0
STR232_CLS			db		"*CLS",CR,LF,0
STR232_SYSTREM			db		":SYST:RWL",CR,LF,0
STR232_CONFV100			db		":SYST:RWL; *CLS; :CONF:VOLT:DC 10, MIN", CR,LF,0
STR232_CONFV10			db		":SYST:RWL; *CLS; :CONF:VOLT:DC 10,0.00001", CR,LF,0
STR232_READ			db		"*CLS;:SYST:RWL;*CLS;:READ?",CR,LF,0

TOTCARAC232			equ		17

	; numero de tentativas de leitura para integracao 10 do multimetro HP 34401A
	; foi medido que o numero de vezes em que se entra na rotina ser232a para
	;	tentar ler o multimetro ate' chegar a resposta e':
	;
	;		integ 10	integ 100
	;		--------	---------
	;		28		321
	;
	; Foram escolhidos como limites do numero de tentativas para se considerar
	; timeout e, assim, minimizar o tempo em que eventualmente o equipamento
	; ficaria travado para respostas/soando o bip:
	;
	;		integ 10	integ 100
	;		--------	---------
	;		48		48 * 8 = 384

MAXTENTAT232			equ		$30


	; ******************************************** ;
	;	Tabela de Comandos para Placa UDC3000	;
	; ******************************************** ;

comand_escSP:			db		",0204,E5,18,039,"		; comando de escrita de Set Point
comand_escPB:			db		",0204,E5,18,001,"		; comando de escrita de Proporcional
comand_escRSET: db		",0204,E5,18,003,"		; comando de escrita de Integrativo
comand_escRATE: db		",0204,E5,18,002,"		; comando de escrita de Derivativo
comand_lei:			db		",0204,E4,18,"		; comando de leitura
comand_122			db		"122,00",13,10
comand_001			db		"001,",13,10
comand_002			db		"002,",13,10
comand_003			db		"003,",13,10

	; converte um asc contido em a para formato do display
tab_01				db		"0",%00111111 ;1
				db		"1",%00000110 ;2
				db		"2",%01011011 ;3
				db		"3",%01001111 ;4
				db		"4",%01100110 ;5
				db		"5",%01101101 ;6
				db		"6",%01111101 ;7
				db		"7",%00000111 ;8
				db		"8",%01111111 ;9
				db		"9",%01101111 ;10
				db		"A",%01110111 ;11
				db		"B",%01111100 ;12
				db		"C",%01011000 ;13
				db		"D",%01011110 ;14
				db		"E",%01111001 ;15
				db		"F",%01110001 ;16
				db		"[",%10111001 ;17
				db		"]",%10001111 ;18
				db		"r",%11010000 ;19
				db		"-",%11000000 ;20
				db		"+",%11110000 ;21
				db		"o",%11011100 ;22
				db		"n",%11010100 ;23
				db		"A",%11110111 ;24
				db		"P",%11110011 ;25
				db		"d",%11011110 ;26
				db		"/",%11001000 ;27
tab_01f				db		0

asc_dsp				push		af		;
				push		hl		;
				push		bc		;
				push		de		;

				ld		hl,tab_01		; inicio da tabela
				ld		de,tab_01f		;
				push		hl		;
asc_dsp3			pop		hl		;
				cp		a,(hl)		;
				jp		z,asc_dsp1		;achou!!
				inc		hl		;
				inc		hl		;
				push		hl		;
				scf		;
				ccf		;
				sbc		hl,de		;
				jp		z,asc_dsp2		;acabou tabela
				jp		asc_dsp3		;proximo!
asc_dsp1			inc		hl		;valor para display
				ld		a,(hl)		;
asc_dsp4			cpl
				ld		(display),a		;
				pop		de
				pop		bc
				pop		hl
				pop		af
				ret
asc_dsp2			pop		hl
				ld		a,$00		;display apagado
				jp		asc_dsp4		;

	;procedure wait100us
	;gera delay de 100 microSeg - destroi conteudo de A
wait100us:			ld		a, (ciclos100us) ;f
wait100us01			dec		a
				nop
				jp		nz,wait100us01
				ret

	;procedure delay01ms
	;gera delay de 1 mSeg
delay01ms:			push		af
				ld		a, 8
delay01ms01			push		af
				call		wait100us
				neg
				neg
				neg
				neg
				neg
				neg
				nop
				nop
				pop		af
				dec		a
				jp		nz, delay01ms01
				pop		af
				ret

	; gera delay de aprox. 3ms
delay03ms:			call		delay01ms
				call		delay01ms
				call		delay01ms
				ret

	;procedure wait10ms
	;gera delay de 10 mSeg
	;existe um pequeno erro no tempo, devido aos ciclos extras (9.995 mSeg)
wait10ms:			push		af
				ld		a,10
wait10ms01			push		af
				call		delay01ms
				pop		af
				dec		a
				jp		nz, wait10ms01
				pop		af
				ret

	;procedure wait50ms
	;gera delay de 60 mSeg
	;existe um pequeno erro no tempo, devido aos ciclos extras (9.995 mSeg)
wait50ms:			call		wait10ms
				call		wait10ms
				call		wait10ms
				call		wait10ms
				call		wait10ms
				ret

	;procedure wait60ms
	;gera delay de 60 mSeg
	;existe um pequeno erro no tempo, devido aos ciclos extras (9.995 mSeg)
wait60ms:			call		wait10ms
				call		wait10ms
				call		wait10ms
				call		wait10ms
				call		wait10ms
				call		wait10ms
				ret

	;procedure wait100ms
	;gera delay de 100 mSeg - destroi conteudo de A
	;existe um pequeno erro no tempo, devido aos ciclos extras (99.995 mSeg)
wait100ms:			push		af
				ld		a,100
wait100ms01			push		af
				call		delay01ms
				pop		af
				dec		a
				jp		nz, wait100ms01
				pop		af
				ret

	;procedure wait300ms
	;gera delay de 300 mSeg
	;existe um pequeno erro no tempo, devido aos ciclos extras (99.995 mSeg)
wait300ms:			call		wait100ms
				call		wait100ms
				call		wait100ms
				ret

	;procedure wait01seg
	;gera delay de 1 Seg
wait01seg			push		af
				ld		a,10
wait01seg01			push		af
				call		wait100ms
				call		wdt
				pop		af
				dec		a
				jp		nz, wait01seg01
				pop		af
				ret

	; envia caracteres via 232 - string apontada por DE e terminada com 0.
	;==============				C e' suposto em end. base
envia_str_232			ld		a, (de)
mandaprox1			inc		de
				out		(c),a		;envio caracter
				call		temt
				ld		a, (de)		;proximo caracter
				cp		0		;e' fim de string?
				ret		z
				jp		mandaprox1
	;===============

	; procedure hex_asc
	; converte um hexa no acumulador em dois ascs, salvos em hl
hex_asc				push		af		;salvo hexa
				srl		a		;
				srl		a		;
				srl		a		;
				srl		a		;
				add		a,$30		;
				cp		a,$3a		;
				jp		m,hex_asc1		;
				add		a,$07		;
hex_asc1			ld		h,a		;salvo primeiro asc
				pop		af		;recupero
				and		a,$0f		;
				add		a,$30		;
				cp		a,$3a		;
				jp		m,hex_asc2		;
				add		a,$07		;
hex_asc2			ld		l,a		;
				ret

	; procedure asc_hex
	; converte dois ascs em h e l em um hexa em a
asc_hex				ld		a,h		;leio asc
				sub		a,$30		;
				cp		a,$0a		;
				jp		m,asc_hex1		;
				sub		a,$07		;
asc_hex1			sla		a		;
				sla		a		;
				sla		a		;
				sla		a		;
				ld		h,a		;
				ld		a,l		;
				sub		a,$30		;
				cp		a,$0a		;
				jp		m,asc_hex2		;
				sub		a,$07		;
asc_hex2			or		a,h		;
				ret


	;**************************************************************************
	;**************************************************************************
	;		Rotinas de gerenciamento das diversas placas
	;**************************************************************************
	;**************************************************************************
	;
	; Os registradores sempre serao utilizados da seguinte forma:
	;	ix - indica a posicao do buffer de leitura das placas (devolve atualizado)
	;	iy - indica a posicao do buffer de escrita nas placas (devolve atualizado)
	;	(base) - indica a porta base da placa em questao ($00, $08, $10, ..., $f0)
	;	(tipo_opr)	- indica o tipo de operacao sendo realizada (OPR_XXXXXX)
	;
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;serial de 2mbps
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

ser2mbps			ret		;nao existe rotina especifica
				;de atualizacao para esta placa


	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;locon 12 bits monopolar com registro de maximo e minimo (L12_MAXMIN)
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

l12maxmin:			ld		a,(base)
				ld		c, a
				ld		a,(tipo_opr)		;tipo de operacao
				cp		OPR_LEITURA		;
				jp		z,l12maxmin0		;leitura
				cp		OPR_ESCRITA		;
				jp		z,l12maxmin1		;escrita
				cp		OPR_PROXIMA		;
				jp		z,l12maxmin2		;passa para a proxima placa
				cp		OPR_BOOT		;
				jp		z,l12maxmin3		;boot do sistema
				ret		;operacao invalida!!

	;---------------------------------------------
	;Leitura da placa - ix
	;---------------------------------------------
l12maxmin0			ld		d, c		;salva endereco base
				inc		c		;01
				inc		c		;02
				inc		c		;03
				inc		c		;04
				out		(c),a		;disparo conversor
				dec		c		;03
				call		wdt		;

l12maxmin01			in		a,(c)		;leio status
				and		a,$01		;
				jp		nz,l12maxmin01 ;

				inc		c		;04
				in		a,(c)		;leio mais sig
				ld		d,a		;
				srl		a		;
				srl		a		;
				srl		a		;
				srl		a		;
				ld		(leittemp+1),a	;escrevo (salvo buffer)
				ld		a,d		;
				sla		a		;
				sla		a		;
				sla		a		;
				sla		a		;
				ld		d,a		;
				inc		c		;05
				in		a,(c)		;leio menos sig
				srl		a		;
				srl		a		;
				srl		a		;
				srl		a		;
				or		d		;
				ld		(leittemp),a	;escrevo (salvo buffer)
				inc		c		;06
				in		a,(c)		;leio status
				ld		(ix+5),a	;escrevo
				call		adicionaserie   ;coloca o valor na serie para integracao
				jp		l12maxmin2	;pula e retorna

	;---------------------------------------------
	;Escrita na placa - iy
	;---------------------------------------------
l12maxmin1			ld		a, (iy)		;recebo a prioridade e coloco no
				ld		(ix), a		;buffer de leitura a prioridade
				ld		a, (iy+1)		;tambem para flag
				ld		(ix+1), a

				ld		a,(iy+2)		;dado + sig
				sla		a		;
				sla		a		;
				sla		a		;
				sla		a		;
				ld		d,a		;
				ld		a,(iy+3)		;dado - sig
				ld		e,a		;
				srl		a		;
				srl		a		;
				srl		a		;
				srl		a		;
				or		d		;
				out		(c),a		;dado + sig (c=00)
				inc		c		;01
				ld		a,e		;
				sla		a		;
				sla		a		;
				sla		a		;
				sla		a		;
				out		(c),a		;dado - sig
				inc		c		;02
				ld		a,(sincrono)
				cp		0
				jp		nz,l12maxmin1a ;se sincrono=1, nao disparo a conversao.Este devera
				;ser feito por uma subrotina no tratamento de int.
				out		(c),a		;converto
l12maxmin1a			inc		c		;03
				inc		c		;04
				inc		c		;05
				inc		c		;06
				ld		a,(iy+4)		;pego controle
				out		(c),a		;escrevo controle
				
				; numero integracoes nao pode passar de MAXINTEG12BITS (1000)
				
				ld		e,(iy+6)        ;pego integracoes-sig
				ld		a,(iy+5)        ;pego integracoes+sig
				ld		d, a								
				; integracoes nao pode passar de MAXINTEG12BITS
				; testo entao o mais significativo
				cp		HIGH MAXINTEG12BITS 
				jp		c, l12maxmin1b  ; e' menor, entao e' aceitavel o num. integ.
				jp		nz, l12maxmin1c	; nao e' menor, nem igual (e' maior - entao despreza)
                                       ; +sig e' igual, devo testar o -sig
                                ld		a, LOW MAXINTEG12BITS
                                cp		e               ; -sig
                                jp		c, l12maxmin1c	; -sig e' maior	que o aceitavel - e' desprezado

				; e' numero valido - se for diferente do que ja' tinha,
				; reinicia a serie de integracoes
l12maxmin1b			
				ld		a, (ix+6)	; compara com o que ja' usava
				cp		d
				jp		nz, mudou	; e' mesmo um novo numero de integracoes
				ld		a, (ix+7)
				cp		e		; compara menos significativo do que estava em uso
				jp		z, l12maxmin1c	;e' o mesmo numero, nada muda

				; mudou o numero de integracoes				
mudou				
				ld		(ix+6),d	;integracoes +sig
 				ld		(ix+7),e	;integracoes -sig
 				call		preenche_serie
				
l12maxmin1c			jp		l12maxmin2		;retorno

;-------------------------- inicializa a serie de dados para integracao

preenche_serie			
				ld		a,(base)
				ld		b,a
				ld		hl, tabseries
				ld		(ninteg), de	  ; salva o numero de integracoes
				
preenche1			ld		a, (hl)
				cp		NAO_INSTALADA     ; chegou ao fim da tabela?
				ret		z		  ; sim, entao nao ha nada a fazer
				cp		b
				jp		z, preenche2	  ; achou end. base?
				ld		de, 7 		  ; nao, passa para a proxima...
				add		hl, de		  ; ...posicao na tabela tabseries	
				jp		preenche1
				
preenche2 			inc		hl		; pula pos. end.base	
				ld		e, (hl)		  ;DE pega o endereco inicial	
				inc		hl		  ;do bloco de memoria correspondente	
				ld		d, (hl)
				inc		hl
				ld		(hl), e		 ; inicializa campo valor atual	do ponteiro
				inc		hl		 ; com o valor do endereco inicial do
				ld		(hl), d		 ; bloco de dados
 				
				
				    ; inicializa ninteg posicoes
				ld		h, d		 ; HL vai apontar para posicao inicial
				ld		l, e		 ;   correspondente no bloco de dados
				ld	        de, (leittemp)   ; valor mais recente de leit. analogica
				ld		bc, (ninteg)	 ; quantas posicoes (de 2 bytes) a preencher
				xor		a
				
preenche3			ld		(hl),d
				inc		hl
				ld		(hl),e
				inc		hl
				dec		bc		
				cp		b
				jp		nz, preenche3
				cp		c
				jp		nz, preenche3
				ret

;===============================
adicionaserie			; preenche num.integ espacos da serie
				; ao mesmo tempo calcula a nova soma de integracao

				ld		a,(base)
				ld		b,a
				ld		hl, tabseries
				
testamo				ld		a, (hl)
				cp		NAO_INSTALADA
				jp		z, eesse
				cp		b
				jp		z, eesse
				
				
eesse				
				ld	hl, (posantinteg)
				ld      b, (hl)
				inc	hl
				ld	c, (hl)
				ld	de, (leittemp) 
				; bc contem o valor mais antigo de leitura
				; de contem o valor atual da leitura 		
				ld	(hl), e
				dec	hl
				ld	(hl), d
				inc	hl
				inc	hl
				
				ld	(posantinteg), hl
				ld	l, (ix+4)	; l:  integ --sig
				ld	h, (ix+3)	; h:  integ +-sig
							; (ix+2):  integ ++sig
				
				scf				; zera carry
				ccf 				; 
				sbc	hl,bc                   ; retira do valor de integracao o mais antigo
				jp	nc, continuasoma
				dec	(ix+2)          	; houve carry, decrementa ++sig
continuasoma			add	hl,de                   ; acrescenta ao valor de integracao a leitura atual
				jp	nc, guardasoma		; houve carry, incrementa ++sig
				inc	(ix+2)
guardasoma			ld	(ix+3), h
				ld	(ix+4), l		; guarda a integracao atualizada
				
				; testa se valor atual DE e' o maior ou o menor
				
				ld	h, (ix+8)
				ld	l, (ix+9)		;valor max
				scf
				ccf				;zera carry
				sbc	hl, de			;
				jp	nc, testamin		;nao e' maior que max? Testa o min
				ld	(ix+8), d		;valor atual passa a ser o max
				ld	(ix+9), e		;

testamin			ld	h, (ix+10)
				ld	l, (ix+11)		;valor min
				scf
				ccf				;zera carry
				sbc	hl, de			;
				jp	c, fimtestes
 				ld	(ix+10), d		;valor atual passa a ser o max
				ld	(ix+11), e		;

fimtestes				
								
				ret				;fim de preencheserie
				
	;---------------------------------------------
	;rotina desloca iy e ix para a proxima placa
	;---------------------------------------------
l12maxmin2			ld		bc, 7
				add		iy, bc
				ld		bc, 12
				add		ix, bc
				ret		;retorno

	;---------------------------------------------
	;rotina especial de boot - iy / ix
	;---------------------------------------------
l12maxmin3			ld		(iy+0),$01		;prioridade
				ld		(iy+1),$80		;flag-$80
				ld		(iy+2),$00		;da +sig-02
				ld		(iy+3),$00		;da -sig-03
				ld		(iy+4),$00		;controle-04
				ld		(iy+5),$00		;integracoes +sig
				ld		(iy+6),$01		;integracoes -sig

				ld		(ix),$01		;prioridade
                                ld		(ix+6),$00		;integracoes +sig
 				ld		(ix+7),$00		;integracoes -sig
				ld		(ix+8),$00		;val. max +sig
 				ld		(ix+9),$00		;val. max -sig
				ld		(ix+10),$FF		;val. min +sig
 				ld		(ix+11),$FF		;val. min -sig
				
;; ocupa para a placa uma posicao na tabela que descreve o bloco 
;; de memoria que guarda as integracoes inicializa o apontador de dado coletado 
 				ld		a,(base)
				ld		b,a
				ld		c,0
				ld		hl, tabseries
				
l12maxmin31			ld		a, c
				cp		MAXSERIESINTEG    ; esta ocupando todos os  
				jp		z, l12maxmin33    ; espaco da tabela?

				inc		c		  ; c = numero de posicoes ocupadas na tabela
				
				ld		a, (hl)
				cp		NAO_INSTALADA  ; espaco esta' disponivel?
				jp		z, l12maxmin32    ; sim
				ld		de, 7 		  ; nao, passa para a proxima	
				add		hl, de		  ; posicao na tabela tabseries	
				jp		l12maxmin31
				
l12maxmin32			ld		(hl), b		  ;ocupa espaco guardando endereco-base
				inc		hl
				ld		e, (hl)		  ;DE pega o endereco inicial	
				inc		hl		  ;do bloco de memoria correspondente	
				ld		d, (hl)
				inc		hl
				ld		(hl), e		 ; inicializa campo valor atual	do ponteiro
				inc		hl		 ; com o valor do endereco inicial do
				ld		(hl), d		 ; bloco de dados
				
				ld		h, d
				ld		l, e
				
				ld		(hl), 0		; inicializa a posicao inicial
				inc		hl		; do bloco de dados da integracao
				ld		(hl), 0		; com o valor de leitura 00
 				
l12maxmin33
 				ld		a,(tam_out)		;
				add		a,$07		;
				ld		(tam_out),a		;
				ld		a,(tam_in)		;
				add		a,$12		;
				ld		(tam_in),a		;

				jp		l12maxmin2




	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;locon 16 bits monopolar
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

l16maxmin			ld		a,(base)
				ld		c,a
				ld		a,(tipo_opr)		;tipo de operacao
				cp		OPR_LEITURA		;
				jp		z,l16maxmin0		;leitura
				cp		OPR_ESCRITA		;
				jp		z,l16maxmin1		;escrita
				cp		OPR_PROXIMA		;
				jp		z,lcn12bmp2		;passa para a proxima placa
				cp		OPR_BOOT		;
				jp		z,l16maxmin3		;boot
				ret		;operacao invalida!
	;-------------------------------------------------
	;Leitura da placa - ix
	;-------------------------------------------------
l16maxmin0			inc		c		;01
				inc		c		;02
				inc		c		;03
				inc		c		;04
				out		(c),a		;disparo conversor
				dec		c		;03
				call		wdt		;
l16maxmin01			in		a,(c)		;leio status
				and		a,$01		;
				jp		nz,l16maxmin01 ;
				inc		c		;04
				out		(c),a		;disparo conversor
				dec		c		;03
				call		wdt		;
l16maxmin02			in		a,(c)		;leio status
				and		a,$01		;
				jp		nz,l16maxmin02 ;disparo duas leituras pois ao AD977BN
				;devolve a leitura da conversao anterior

				inc		c		;04
				in		a,(c)		;leio mais sig
				ld		(ix+2),a		;escrevo
				inc		c		;05
				in		a,(c)		;leio menos sig
				ld		(ix+3),a		;escrevo
				inc		c		;06
				in		a,(c)		;leio status
				ld		(ix+4),a		;escrevo

				jp		lcn12bmp2		;pula e retorna

	;-------------------------------------------------
	;Escrita na placa - iy
	;-------------------------------------------------

l16maxmin1			ld		a, (iy)		;recebo a prioridade da placa
				ld		(ix), a		;e a coloco no buffer de leitura
				ld		a, (iy+1)
				ld		(ix+1), a		;tambem para flag

				ld		a,(iy+2)		;
				out		(c),a		;mais sig (end. base)
				inc		c		;01
				ld		a,(iy+3)		;
				out		(c),a		;dado - sig

				ld		a,(sincrono)
				cp		0
				jp		nz,l16maxmin1a
				dec		c		;base
				in		a, (c)		;(end. base) le configuracao de
				ld		d, a		;offset
				inc		c		;(base + 1)
				in		a, (c)		;le configuracao de ganho
				inc		c		;(base + 2)
				out		(c), a		;ajusta ganho
				inc		c
				inc		c
				inc		c		;(base + 5)
				out		(c), d		;ajusta offset
				inc		c		;06
				ld		a,(iy+4)		;pego controle
				out		(c),a		;escrevo controle
				jp		lcn12bmp2		;pula e retorna

l16maxmin1a			inc		c		;02
				inc		c		;03
				inc		c		;04
				inc		c		;05
				inc		c		;06
				ld		a,(iy+4)		;pego controle
				out		(c),a		;escrevo controle
				jp		lcn12bmp2		;pula e retorna

	;-------------------------------------------------
	;rotina especial de boot - iy / ix
	;-------------------------------------------------
l16maxmin3			ld		(iy),$01		;prioridade-00
				ld		(iy+1),$80		;flag-$80
				ld		(iy+2),$00		;da +sig-02
				ld		(iy+3),$00		;da -sig-03
				ld		(iy+4),$00		;controle-04

				ld		(ix),$01		;prioridade-00

				;Ajuste do offset e ganho para os conversores:	
				;
				in		a, (c)		;(end. base) le configuracao de
				ld		d, a		;offset
				inc		c		;(base + 1)
				in		a, (c)		;le configuracao de ganho
				inc		c		;(base + 2)
				out		(c), a		;ajusta ganho
				inc		c
				inc		c
				inc		c		;(base + 5)
				out		(c), d		;ajusta offset

				ld		a,(tam_out)		;
				add		a,$05		;
				ld		(tam_out),a		;
				ld		a,(tam_in)		;
				add		a,$05		;
				ld		(tam_in),a		;
				jp		lcn12bmp2



	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;locon 12 bits monopolar
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

lcn12bmp:			ld		a,(base)
				ld		c, a
				ld		a,(tipo_opr)		;tipo de operacao
				cp		OPR_LEITURA		;
				jp		z,lcn12bmp0		;leitura
				cp		OPR_ESCRITA		;
				jp		z,lcn12bmp1		;escrita
				cp		OPR_PROXIMA		;
				jp		z,lcn12bmp2		;passa para a proxima placa
				cp		OPR_BOOT		;
				jp		z,lcn12bmp3		;boot do sistema
				ret		;operacao invalida!!

	;---------------------------------------------
	;Leitura da placa - ix
	;---------------------------------------------
lcn12bmp0			ld		d, c		;salva endereco base
				inc		c		;01
				inc		c		;02
				inc		c		;03
				inc		c		;04
				out		(c),a		;disparo conversor
				dec		c		;03
				call		wdt		;

lcn12bmp01			in		a,(c)		;leio status
				and		a,$01		;
				jp		nz,lcn12bmp01 ;

				inc		c		;04
				in		a,(c)		;leio mais sig
				ld		d,a		;
				srl		a		;
				srl		a		;
				srl		a		;
				srl		a		;
				ld		(ix+2),a		;escrevo (salvo buffer)
				ld		a,d		;
				sla		a		;
				sla		a		;
				sla		a		;
				sla		a		;
				ld		d,a		;
				inc		c		;05
				in		a,(c)		;leio menos sig
				srl		a		;
				srl		a		;
				srl		a		;
				srl		a		;
				or		d		;
				ld		(ix+3),a		;escrevo (salvo buffer)
				inc		c		;06
				in		a,(c)		;leio status
				ld		(ix+4),a		;escrevo
				jp		lcn12bmp2		;pula e retorna

	;---------------------------------------------
	;Escrita na placa - iy
	;---------------------------------------------
lcn12bmp1			ld		a, (iy)		;recebo a prioridade e coloco no
				ld		(ix), a		;buffer de leitura a prioridade
				ld		a, (iy+1)		;tambem para flag
				ld		(ix+1), a

				ld		a,(iy+2)		;dado + sig
				sla		a		;
				sla		a		;
				sla		a		;
				sla		a		;
				ld		d,a		;
				ld		a,(iy+3)		;dado - sig
				ld		e,a		;
				srl		a		;
				srl		a		;
				srl		a		;
				srl		a		;
				or		d		;
				out		(c),a		;dado + sig (c=00)
				inc		c		;01
				ld		a,e		;
				sla		a		;
				sla		a		;
				sla		a		;
				sla		a		;
				out		(c),a		;dado - sig
				inc		c		;02
				ld		a,(sincrono)
				cp		0
				jp		nz,lcn12bmp1a ;se sincrono=1, nao disparo a conversao.Este devera
				;ser feito por uma subrotina no tratamento de int.
				out		(c),a		;converto
lcn12bmp1a			inc		c		;03
				inc		c		;04
				inc		c		;05
				inc		c		;06
				ld		a,(iy+4)		;pego controle
				out		(c),a		;escrevo controle
				jp		lcn12bmp2		;retorno

	;---------------------------------------------
	;rotina desloca iy e ix para a proxima placa
	;---------------------------------------------
lcn12bmp2			ld		bc, 5
				add		iy, bc
				add		ix, bc
				ret		;retorno

	;---------------------------------------------
	;rotina especial de boot - iy / ix
	;---------------------------------------------
lcn12bmp3			ld		(iy+0),$01		;prioridade-00
				ld		(iy+1),$80		;flag-$80
				ld		(iy+2),$00		;da +sig-02
				ld		(iy+3),$00		;da -sig-03
				ld		(iy+4),$00		;controle-04

				ld		(ix),$01		;prioridade-00

				ld		a,(tam_out)		;
				add		a,$05		;
				ld		(tam_out),a		;
				ld		a,(tam_in)		;
				add		a,$05		;
				ld		(tam_in),a		;

				jp		lcn12bmp2

	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;locon 12 bits bipolar
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
lcn12bbp			ld		a,(base)
				ld		c, a
				ld		a,(tipo_opr)		;tipo de operacao
				cp		OPR_LEITURA		;
				jp		z,lcn12bmp0		;leitura
				cp		OPR_ESCRITA		;
				jp		z,lcn12bmp1		;escrita
				cp		OPR_PROXIMA		;
				jp		z,lcn12bmp2		;passa para a proxima placa
				cp		OPR_BOOT		;
				jp		z,lcn12bbp3		;boot do sistema
				ret		;operacao invalida!!
	;-------------------------------------------------
	;rotina especial de boot - iy / ix - exclusiva para bipolar
	;-------------------------------------------------
lcn12bbp3			ld		(iy+0),$01		;prioridade-00
				ld		(iy+1),$80		;flag-$80
				ld		(iy+2),$07		;da +sig-02
				ld		(iy+3),$ff		;da -sig-03
				ld		(iy+4),$00		;controle-04

				ld		(ix),$01		;prioridade-00

				ld		a,(tam_out)		;
				add		a,$05		;
				ld		(tam_out),a		;
				ld		a,(tam_in)		;
				add		a,$05		;
				ld		(tam_in),a		;

				jp		lcn12bmp2		;pula e retorna

	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;locon 16 bits monopolar
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

lcn16bmp			ld		a,(base)
				ld		c,a
				ld		a,(tipo_opr)		;tipo de operacao
				cp		OPR_LEITURA		;
				jp		z,lcn16bmp0		;leitura
				cp		OPR_ESCRITA		;
				jp		z,lcn16bmp1		;escrita
				cp		OPR_PROXIMA		;
				jp		z,lcn12bmp2		;passa para a proxima placa
				cp		OPR_BOOT		;
				jp		z,lcn16bmp3		;boot
				ret		;operacao invalida!
	;-------------------------------------------------
	;Leitura da placa - ix
	;-------------------------------------------------
lcn16bmp0			inc		c		;01
				inc		c		;02
				inc		c		;03
				inc		c		;04
				out		(c),a		;disparo conversor
				dec		c		;03
				call		wdt		;
lcn16bmp01			in		a,(c)		;leio status
				and		a,$01		;
				jp		nz,lcn16bmp01 ;
				inc		c		;04
				out		(c),a		;disparo conversor
				dec		c		;03
				call		wdt		;
lcn16bmp02			in		a,(c)		;leio status
				and		a,$01		;
				jp		nz,lcn16bmp02 ;disparo duas leituras pois ao AD977BN
				;devolve a leitura da conversao anterior

				inc		c		;04
				in		a,(c)		;leio mais sig
				ld		(ix+2),a		;escrevo
				inc		c		;05
				in		a,(c)		;leio menos sig
				ld		(ix+3),a		;escrevo
				inc		c		;06
				in		a,(c)		;leio status
				ld		(ix+4),a		;escrevo

				jp		lcn12bmp2		;pula e retorna

	;-------------------------------------------------
	;Escrita na placa - iy
	;-------------------------------------------------

lcn16bmp1			ld		a, (iy)		;recebo a prioridade da placa
				ld		(ix), a		;e a coloco no buffer de leitura
				ld		a, (iy+1)
				ld		(ix+1), a		;tambem para flag

				ld		a,(iy+2)		;
				out		(c),a		;mais sig (end. base)
				inc		c		;01
				ld		a,(iy+3)		;
				out		(c),a		;dado - sig

				ld		a,(sincrono)
				cp		0
				jp		nz,lcn16bmp1a
				dec		c		;base
				in		a, (c)		;(end. base) le configuracao de
				ld		d, a		;offset
				inc		c		;(base + 1)
				in		a, (c)		;le configuracao de ganho
				inc		c		;(base + 2)
				out		(c), a		;ajusta ganho
				inc		c
				inc		c
				inc		c		;(base + 5)
				out		(c), d		;ajusta offset
				inc		c		;06
				ld		a,(iy+4)		;pego controle
				out		(c),a		;escrevo controle
				jp		lcn12bmp2		;pula e retorna

lcn16bmp1a			inc		c		;02
				inc		c		;03
				inc		c		;04
				inc		c		;05
				inc		c		;06
				ld		a,(iy+4)		;pego controle
				out		(c),a		;escrevo controle
				jp		lcn12bmp2		;pula e retorna

	;-------------------------------------------------
	;rotina especial de boot - iy / ix
	;-------------------------------------------------
lcn16bmp3			ld		(iy),$01		;prioridade-00
				ld		(iy+1),$80		;flag-$80
				ld		(iy+2),$00		;da +sig-02
				ld		(iy+3),$00		;da -sig-03
				ld		(iy+4),$00		;controle-04

lcn16bmp30			ld		(ix),$01		;prioridade-00

				;Ajuste do offset e ganho para os
;conversores:	
				;
				in		a, (c)		;(end. base) le configuracao de
				ld		d, a		;offset
				inc		c		;(base + 1)
				in		a, (c)		;le configuracao de ganho
				inc		c		;(base + 2)
				out		(c), a		;ajusta ganho
				inc		c
				inc		c
				inc		c		;(base + 5)
				out		(c), d		;ajusta offset

				ld		a,(tam_out)		;
				add		a,$05		;
				ld		(tam_out),a		;
				ld		a,(tam_in)		;
				add		a,$05		;
				ld		(tam_in),a		;
				jp		lcn12bmp2

	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;locon 16 bits versao 3.2 Monopolar na escrita e Monopolar na leitura(MM)
	;Como esta versao so possui configuracao por hardware como bipolar na
	;leitura, entao e feito uma transformacao da leitura bipolar em monopolar
	;via software segundo a equacao:
	;valor monopolar = (valor bipolar - 32768) * 2.
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

lcn16b32mm:
				ld		a,(base)
				ld		c,a
				ld		a,(tipo_opr)		;tipo de operacao
				cp		OPR_LEITURA		;
				jp		z,lcn16b32mm0 ;leitura
				cp		OPR_ESCRITA		;
				jp		z,lcn16bmp1		;escrita
				cp		OPR_PROXIMA		;
				jp		z,lcn12bmp2		;passa para a proxima placa
				cp		OPR_BOOT		;
				jp		z,lcn16bmp3		;boot
				ret		;operacao invalida!


	;-------------------------------------------------
	;Leitura da placa - ix - exclusiva para v. 3.2
	;-------------------------------------------------
lcn16b32mm0:			inc		c		;01
				inc		c		;02
				inc		c		;03
				inc		c		;04
				out		(c),a		;disparo conversor
				dec		c		;03
				call		wdt		;

lcn16b32mm01:			in		a,(c)		;leio status
				and		a,$01		;
				jp		nz,lcn16b32mm01 ;

				push		hl
				push		de
				inc		c		;04
				in		a,(c)		;leio mais sig
				ld		h,a
				inc		c		;05
				in		a,(c)		;leio menos sig
				ld		l,a
				ld		de,$8000

				xor		a		;zera carry
				sbc		hl,de

				jp		c,lcn16b32mm02; se valor lido < 0
				ld		a,l
				sla		a
				ld		(ix+3),a
				ld		a,h
				rla
				ld		(ix+2),a
				jp		lcn16b32mm03

lcn16b32mm02:			ld		(ix+2),0 ; entao valor monopolar = 0
				ld		(ix+3),0

lcn16b32mm03:			inc		c		;06
				in		a,(c)		;leio status
				ld		(ix+4),a		;escrevo
				pop		de
				pop		hl

				jp		lcn12bmp2		;pula e retorna

	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;locon 16 bits bipolar
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
lcn16bbp			ld		a,(base)
				ld		c,a
				ld		a,(tipo_opr)		;tipo de operacao
				cp		OPR_LEITURA		;
				jp		z,lcn16bmp0		;leitura
				cp		OPR_ESCRITA		;
				jp		z,lcn16bmp1		;escrita
				cp		OPR_PROXIMA		;
				jp		z,lcn12bmp2		;passa para a proxima placa
				cp		OPR_BOOT		;
				jp		z,lcn16bbp3		;boot
				ret		;operacao invalida!

	;-------------------------------------------------------------------
	;rotina especial de boot - iy / ix - exclusiva para bipolar 16 bits
	;-------------------------------------------------------------------
lcn16bbp3			ld		(iy+0),$01		;prioridade-00
				ld		(iy+1),$80		;flag-$80
				ld		(iy+2),$7f		;da +sig-02
				ld		(iy+3),$ff		;da -sig-03
				ld		(iy+4),$00		;controle-04
				jp		lcn16bmp30		;continua o boot como monopolar

	;################################################
	;# Faz leitura da LCN16 bits visando a integracao
	;# retorna:	DE = leitura BPM X
	;#		(LeitY) = leitura BPM Y
	;################################################

integLCN16			ld		a, (endbaseBPM2)		;end.base+6
				ld		c, a
				dec		c		;05
				dec		c		;04
				out		(c),a		;disparo conversor placa Y
				ld		h, c		;guardo em H end.base+4

				ld		a, (endbaseBPM1)		;end.base+6
				ld		c, a
				dec		c		;05
				dec		c		;04
				out		(c),a		;disparo conversor placa X
				ld		l, c		;guardo em L end.base+4

				dec		c		;03
				call		wdt
integ1601			in		a,(c)		;leio status placa X
				and		a,$01		;
				jp		nz,integ1601		;

				inc		c		;04
				out		(c),a		;disparo conversor placa X

				ld		c, h		;recupero end.base+4 de Y
				out		(c), a		;disparo conversor placa Y

				dec		c		;03
				call		wdt
integ1602			in		a,(c)		;leio status placa Y
				and		a,$01		;aguardo conversor Y
				jp		nz,integ1602
				;disparo duas leituras pois ao AD977BN
				;devolve a leitura da conversao anterior

				inc		c		;04
				in		a,(c)		;leio +sig Y
				ld		d, a		;guardo +sig Y
				inc		c		;05
				in		a,(c)		;leio -sig Y
				ld		e, a		;guardo -sig Y

				ld		(leitY), de		;guardo placa Y

				ld		c, l		;recupero end.base+4 de X
				in		a,(c)		;leio +sig X
				ld		d, a		;guardo +sig X
				inc		c		;05
				in		a,(c)		;leio -sig X
				ld		e, a		;guardo -sig X
				ret

	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;BPM64
	;locon 16 bits versao 3.2 Monopolar na leitura e sem ajuste
	;Esta e' utilizada para leitura dos novos BPM, com integracao de 64 leituras
	;valor monopolar = (valor bipolar - 32768) * 2.
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

bpm64:
				ld		a,(base)
				ld		c,a
				ld		a,(tipo_opr)		;tipo de operacao
				cp		OPR_LEITURA		;
				jp		z,bpm64_0		;leitura integrada
				cp		OPR_ESCRITA		;
				jp		z,bpm64_1		;escrita
				cp		OPR_PROXIMA		;
				jp		z,bpm64_2		;passa para a proxima placa
				cp		OPR_BOOT		;
				jp		z,bpm64_3		;boot
				ret		;operacao invalida!

	;-------------------------------------------------
	;Leitura da placa - ix
	;-------------------------------------------------

bpm64_0				call		wdt

				ld		a, (modorampaBPM)
				cp		1
				jp		nz, veio_normal
				ld		a, DISPLAY_SUBIDA
				ld		(display), a
				ld		a, BLOCO_ENTRADA
				ld		c, CMD_NORMAL
				call		Sinaliza		;limpo o byte de comando de ajuste

				ld		a, (tam_in)
				cp		231		;evita que seja excedido o tamanho de 242
				jp		nc, fim_bpm64

veio_normal			xor		a
				ld		(cont_vez), a

				ld		a, (masc_BPM64)
				ld		(chave_BPM), a

				bit		0, a		;vai ter que ler BPM1? bit 0: le (X1,Y1)
				jp		z, so_BPM64_02		;Nao!

	;---- chaveia para BPM 1
				ld		a, (endbaseBPM1)
				ld		c, a
				ld		a, 1		;carrega a chave
				out		(c), a		;escreve na porta digital, chaveando eletronica
				;		que seleciona BPM
				call		wait60ms		; (estabilizacao da eletronica)

trata_bpmXeY			ld		iy, (ptdadosBPM)		;ponteiro para a coleta de orbita
				ld		(iy+0), 0		;integracao detetor Xij ++sig
				ld		(iy+1), 0		;		+-sig
				ld		(iy+2), 0		;		--sig
				ld		(iy+3), 0		;integracao detetor Yij ++sig
				ld		(iy+4), 0		;		+-sig
				ld		(iy+5), 0		;		--sig

				xor		a		;a:= 0
				ld		(n_loopBPM), a		;inicia uma nova integracao

	;---- faz para BPM X

trata_bpmX			;inicializa espaco para X e Y

				call		integlcn16		;DE:= ultima leitura AD det X
				ld		h, (iy+1)		;valor de soma da integ. detetor Xij
				ld		l, (iy+2)
				add		hl, de		;acrescenta a ultima leitura do X
				ld		(iy+1), h		;atualiza soma de integracao do det Xij
				ld		(iy+2), l
				jp		nc, trata_bpmY		;carry?
				inc		(iy)		;sim: soma carry na
				;		integracao do AD++sig
	;---- faz para BPM Y

trata_bpmY			ld		h, (iy+4)		;valor de soma da integ. detetor Yij
				ld		l, (iy+5)
				ld		de, (leitY)		;DE:= ultima leitura AD det Y
				add		hl, de		;acrescenta a ultima leitura do Y
				ld		(iy+4), h		;atualiza soma de integracao do det Yij
				ld		(iy+5), l
				jp		nc, trata_bpm03		;carry?
				inc		(iy+3)		;sim: atualiza valor ++sig integracao

	;---- verifica se ja' completou a integracao em questao

trata_bpm03			ld		a, (n_integBPM64)		;numero esperado de integracoes para a BPM
				ld		b, a
				ld		a, (n_loopBPM)		;quantas iteracoes ja' feitas na integracao
				inc		a
				cp		b
				jp		z, trata_bpm64		;chegou na ultima?
				ld		(n_loopBPM), a		;nao, entao
				jp		trata_bpmX		;		vai para a proxima iteracao

	;---- ja' completou a integracao

trata_bpm64			ld		a, (tam_in)		;aumenta o tamanho dos dados
				add		a, 6		;		enviados para o micro
				ld		(tam_in), a

				ld		bc, 6		;pula para o espaco do prox. detetor
				add		iy, bc
				ld		(ptdadosBPM), iy		;aponta para o espaco da proxima integracao

				ld		a, (cont_vez)
				inc		a
				ld		(cont_vez), a

	;precisa chavear para BPM2?
				cp		2
				jp		z, fim_bpm64

				ld		a, (chave_BPM)
				bit		1, a		;vai ter que ler BPM2? bit 1: le (X2,Y2)
				jp		nz, faz_BPM64_02
				; Nao!
				jp		fim_bpm64

so_BPM64_02			ld		iy, (ptdadosBPM)		;ponteiro para a coleta de orbita
				ld		(iy), 0		;integracao detetor Xij ++sig
				ld		(iy+1), 0		;		+-sig
				ld		(iy+2), 0		;		--sig
				ld		(iy+3), 0		;integracao detetor Yij ++sig
				ld		(iy+4), 0		;		+-sig
				ld		(iy+5), 0		;		--sig
				jp		trata_BPM64


faz_BPM64_02			ld		a, (endbaseBPM1)
				ld		c, a
				xor		a		;carrega a chave com zero
				out		(c), a		;escreve na porta digital, chaveando eletronica
				;		que seleciona BPM
				call		wait60ms		; (estabilizacao da eletronica)
				jp		trata_bpmXeY

				;---- ja' fez a integracao em X e Y para cada um dos dois BPM do bastidor

fim_bpm64			ret

	;---------------------------------------------
	;Escrita na placa BPM64 - iy
	;---------------------------------------------
bpm64_1				di

				ld		a, (iy)		;recebo a prioridade e coloco no
				ld		(ix), a		;buffer de leitura
				inc		iy		;prioridade
				inc		ix
				ld		a, (iy)		;tambem para flag
				ld		(ix), a
				inc		ix
				inc		iy

				ld		a,(iy)		;param: #integracoes da BPM
				inc		iy		;proximo param.
				cp		0		;modo NORMAL?
				jp		nz, bpm64_100
				inc		a		;numero minimo de integ = 1
bpm64_100			ld		(n_integBPM64), a
				ld		a,(iy)		;param: modo de operacao
				inc		iy		;proximo param.
				cp		0		;modo NORMAL?
				jp		nz, bpm64_11

	;---- modo NORMAL
				ld		a, (iy)		;mascara de chaveamento
				inc		iy
				cp		4		;sao validos: 1, 2 ou 3(ambos)?
				jp		c, bpm64_110
				ld		a, 3		;se nao e' valido, guarda o
				;default (3 = ambos)
				ld		iy, buf_in+2
				ld		(ptdadosBPM), iy		;ponteiro de orbitas no inicio


bpm64_110			ld		(masc_BPM64), a		;guarda em masc_BPM64
				xor		a
				ld		(modorampaBPM), a		;indica que nao esta' mais no
				;		modo rampa

				ld		a, (buf_in)		;prioridade da primeira placa
				cpl		;
				ld		(display), a
				ld		a, BLOCO_ENTRADA
				ld		c, CMD_NORMAL
				call		Sinaliza		;limpo o byte de comando de ajuste
				jp		bpm64_12

	;---- modo RAMPA
bpm64_11			cp		1
				jp		nz, bpm64_12		;modo RAMPA?
				inc		iy
				ld		a, (iy)		;passos entre duas coletas
				cp		0
				jp		nz, bpm64_111		;deve ser no minimo 1
				inc		a		;
bpm64_111			ld		(pulsos_entreBPM), a		;guarda o numero de passos

				ld		a, %11		;chaveara' ambos
				ld		(masc_BPM64), a
				ld		(chave_BPM),a		;zera a variavel chaveadora

				ld		iy, buf_in+2
				ld		(ptdadosBPM), iy		;ponteiro de orbitas no inicio
				;busca no mapa de placas

				xor		a		;A:= 0
				ld		(integracoesBPM), a		;zera contador de integracoes
				ld		(sincrono), a
				inc		a		;A:= 1
				ld		(conta_pulsosBPM), a		;para fazer a coleta assim que
				ld		(modorampaBPM),		a		;recebe o primeiro pulso
				;inicializa cont. pulsos para cada
				ld		(conta_passosBPM), a		;		passo; feito = 1 para coletar assim
				; que recebe o primeiro pulso
				inc		a		;A:= 2
				ld		(tam_in), a		;tamanho do bloco (apenas as 2 placas)
				ld		a, INT_BPM		;prepara para tratar as interrupcoes
				ld		(interrupt), a

bpm64_12			ld		a, (modorampabpm)
				cp		0
				ret		z

				ei
				ret		;retorno

	;---------------------------------------------
	;rotina desloca iy e ix para a proxima placa
	;---------------------------------------------
bpm64_2				ld		b, 0

				ld		a, (tam_in)
				ld		c, a
				add		ix, bc

				ld		a, (tam_out)
				ld		c, a
				add		iy, bc
				ret		;retorno

	;---------------------------------------------
	;rotina especial de boot - iy / ix
	;---------------------------------------------
bpm64_3				ld		(iy),$01		;prioridade-00
				ld		(iy+1),$80		;flag-$80

				ld		(ix),$01		;prioridade-00
				ld		(ix+1),$80		;flag-$80

				xor		a
				ld		(iy+2), a		;
				ld		(modorampaBPM), a

				ld		a, %11
				ld		(masc_BPM64), a
				ld		(iy+3), a		;

				ld		a, 64		; default
				ld		(n_integBPM64), a
				ld		(iy+4), a		;

				ld		a, 1
				ld		(iy+5), a		;

				ld		(pulsos_entreBPM), a
				ld		(conta_pulsosBPM), a
				ld		(conta_passosBPM), a

				ld		a,(tam_out)		;
				add		a,$06		;
				ld		(tam_out),a		;

				ld		a,(tam_in)		;
				add		a,$02		;
				ld		(tam_in),a		;

				ld		bc, 06
				add		iy, bc
				inc		ix
				inc		ix
				ret

	;;;;;;;;;;

	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;locon 12 bits add -> entrega soma de 16 leituras
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

lcn12add			ld		a,(base)
				ld		c, a
				ld		a,(tipo_opr)		;tipo de operacao
				cp		OPR_LEITURA		;
				jp		z,lcn12add0		;leitura
				cp		OPR_ESCRITA		;
				jp		z,lcn12add1		;escrita
				cp		OPR_PROXIMA		;
				jp		z,lcn12add2		;passa para a proxima placa
				cp		OPR_BOOT		;
				jp		z,lcn12add3		;boot do sistema
				ret		;operacao invalida!!

	;---------------------------------------------
	;Leitura da placa - ix
	;---------------------------------------------
lcn12add0			inc		c		;01
				inc		c		;02
				inc		c		;03
				inc		c		;04

				xor		a
				ld		(hsd),a		;
				ld		(lsd),a		;inicializa integradores
				ld		b, 16		;numero de vezes da integracao

	;--------------- integracao para se atingir 16 bits

lcn12add01			out		(c),a		;disparo conversor
				dec		c		;03
				call		wdt		;

lcn12add02			in		a,(c)		;leio status
				and		a,$01		;para aguardar conversao
				jp		nz,lcn12add02 ;

				in		a,(c)		;leio mais sig
				ld		d,a		;
				srl		a
				srl		a
				srl		a
				srl		a		;pego os 4 bits mais sig justificados a direita
				ld		e,a		;e salvo em e
				ld		a,d		;recupero mais sig
				sla		a		;
				sla		a		;
				sla		a		;
				sla		a		;pego os 4 bits inferiores justificados a esquerda
				ld		d,a		;e salvo em d
				inc		c		;05
				in		a,(c)		;leio menos sig
				srl		a		;
				srl		a		;
				srl		a		;
				srl		a		;justifico a direita
				or		d		;faco or com os justificados a esquerda
				ld		d,a		;e salvo em d
				ld		a,(lsd)		;pego dado -sig da leitura anterior
				add		a,d		;somo com dado -sig da leitura atual
				ld		(lsd),a		;e salvo novamente em buffer de dado -sig
				ld		a,(hsd)		;pego dado +sig da leitura anterior
				adc		a,e		;somo com dado +sig da leitura atual + carry
				ld		(hsd),a		;e salvo novamente em buffer para dado +sig

				dec		c		;04
				dec		b		;contador da integracao
				;ja fez a integracao de 16 leituras?
				jp		nz,lcn12add01

	;------------------

				ld		a,(hsd)		;pego dado +sig da soma de 16 leituras
				ld		(ix+2),a		;e salvo em ix +sig
				ld		a,(lsd)		;pego dado -sig da soma de 16 leituras
				ld		(ix+3),a		;e salvo em ix -sig
				inc		c		;06
				in		a,(c)		;leio status
				ld		(ix+4),a		;escrevo
				jp		lcn12add2		;retorno

	;---------------------------------------------
	;Escrita na placa - iy
	;---------------------------------------------
lcn12add1			jp		lcn12bmp1

	;---------------------------------------------
	;rotina desloca iy e ix para a proxima placa
	;---------------------------------------------
lcn12add2			ld		bc, 5
				add		ix, bc
				add		iy, bc
				ret		;retorno

	;---------------------------------------------
	;rotina especial de boot - iy / ix
	;---------------------------------------------
lcn12add3			ld		(iy+0),$01		;prioridade-00
				ld		(iy+1),$80		;flag-$80
				ld		(iy+2),$00		;da +sig-02
				ld		(iy+3),$00		;da -sig-03
				ld		(iy+4),$00		;controle-04

				ld		(ix),$01		;prioridade-00

				ld		a,(tam_out)		;
				add		a,$05		;
				ld		(tam_out),a		;
				ld		a,(tam_in)		;
				add		a,$05		;
				ld		(tam_in),a		;

				jp		lcn12add2		;pula e retorna


	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;locomux 12 bits monopolar
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
mux12bmp			;		ld		a,(base)
				;		ld		c,a
				;		ld		a,(tipo_opr)		;tipo de operacao
				;		cp		OPR_LEITURA		;
				;		jp		z,mux12bmp0		;leitura
				;		cp		OPR_ESCRITA		;
				;		jp		z,mux12bmp1		;escrita
				;		cp		OPR_PROXIMA		;
				;		jp		z,mux12bmp2		;passa para a proxima placa
				;		cp		OPR_BOOT		;
				;		jp		z,mux12bmp3		;boot
				ret		;operacao nao definida

	;-------------------------------------------------
	;Leitura da placa - ix
	;-------------------------------------------------

mux12bmp0			;		inc		ix	;prioridade
				;		inc		ix		;flag
				;		push		bc		;
				;		call		wait01seg		;espera de 1 segundo
				;		inc		c		;01
				;		inc		c		;02
				;		inc		c		;03
				;		inc		c		;04
				;		inc		c		;05
				;		inc		c		;06
				;		ld		b,%11000001		;placa nova e velha
mux12bmp01 ;			push		bc		;vou utilizar b e c
				;		out		(c),b		;seleciono canal
				;		call		w0100		;espero 100 mSeg
				;		dec		c		;05
				;		dec		c		;04
				;		out		(c),a		;disparo
				;		dec		c		;03
				;		call		wdt		;

mux12bmp02 ;			in		a,(c)		;leio status
				;		and		a,$01		;
				;		jp		nz,mux12bmp02 ;

				;		inc		c		;04
				;		in		a,(c)		;leio +sig
				;		ld		d,a		;
				;		srl		a		;
				;		srl		a		;
				;		srl		a		;
				;		srl		a		;
				;		ld		(ix),a		;escrevo
				;		inc		ix		;
				;		ld		a,d		;
				;		sla		a		;
				;		sla		a		;
				;		sla		a		;
				;		sla		a		;
				;		ld		d,a		;
				;		inc		c		;05
				;		in		a,(c)		;leio -sig
				;		srl		a		;
				;		srl		a		;
				;		srl		a		;
				;		srl		a		;
				;		or		d		;
				;		ld		(ix),a		;escrevo
				;		inc		ix		;
				;		pop		bc		;c=06 e b=ultimo canal
				;		ld		a,b		;
				;		and		a,$0f		;
				;		sla		a		;
				;		ld		d,a		;salvo
				;		ld		a,b		;
				;		and		$f0		;
				;		sub		$40
				;		or		d		;novo canal
				;		ld		b,a		;b=novo canal
				;		ld		a,d		;
				;		cp		a,$10		;todo canais ja' lidos
				;		jp		nz,mux12bmp01 ;proximo canal
				;		in		a,(c)		;leio status
				;		ld		(ix),a		;salvo
				;		inc		ix		;
				;		pop		bc		;recupero
				;		inc		iy
				;		inc		iy
				;		inc		iy
				ret

	;-------------------------------------------------
	;Escrita na placa - iy
	;-------------------------------------------------
mux12bmp1			;		ld		a, (iy)	;recebo a prioridade da placa
				;		ld		(ix), a		;e a coloco no buffer de leitura
				;		inc		iy		;
				;		inc		ix
				;		ld		a, (iy)		;tambem para flag
				;		ld		(ix), a
				;		inc		iy		;
				;		ld		a,(iy)		;
				;		inc		iy		;
				;		out		(c),a		;escrevo saida digital (c=00)
				;		%rept		10
				;		inc		ix
				;		%endrept
				ret
	;-------------------------------------------------
	;rotina desloca iy e ix para a proxima placa
	;-------------------------------------------------
mux12bmp2			;
				;		%rept		11
				;		inc		ix
				;		%endrept
				;		inc		iy
				;		inc		iy
				;		inc		iy
				ret		;retorno

	;-------------------------------------------------
	;rotina especial de boot - iy / ix
	;-------------------------------------------------
mux12bmp3			;		ld		(iy),$01	;prioridade-00
				;		inc		iy		;
				;		ld		(iy),$80		;flag-$80
				;		inc		iy		;
				;		ld		(iy),$00		;controle-02
				;		inc		iy		;proxima placa
				;		ld		(ix),$01		;prioridade-00
				;		%rept		11
				;		inc		ix		;01..10 e prox. placa
				;		%endrept
				;		ld		a,(tam_out)		;
				;		add		a,3		;
				;		ld		(tam_out),a		;
				;		ld		a,(tam_in)		;
				;		add		a,11		;
				;		ld		(tam_in),a		;
				ret

	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;locomux 12 bits bipolar
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
mux12bbp			jp		mux12bmp		;rotinas sao equivalentes

	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;RUX12BMP - locomux 12 bits monopolar rapida
	;		(10 ms. entre cada porta e nenhum tempo
	;		de espera entre placas)
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

rux12bmp			ld		a,(base)
				ld		c,a
				ld		a,(tipo_opr)		;tipo de operacao
				cp		OPR_LEITURA		;
				jp		z,Rux12bmp0		;leitura
				cp		OPR_ESCRITA		;
				jp		z,rux12bmp1		;escrita (= MUX)
				cp		OPR_PROXIMA		;
				jp		z,rux12bmp2		;passa para a proxima placa (= MUX)
				cp		OPR_BOOT		;
				jp		z,rux12bmp3		;boot (= MUX)
				ret		;operacao nao definida

	;-------------------------------------------------
	;Leitura da placa - ix
	;-------------------------------------------------
rux12bmp0			inc		ix		;prioridade
				inc		ix		;flag
				push		bc		;
				inc		c		;01
				inc		c		;02
				inc		c		;03
				inc		c		;04
				inc		c		;05
				inc		c		;06
				ld		b,%11000001		;placa nova e velha
rux12bmp01			push		bc		;vou utilizar b e c
				out		(c),b		;seleciono canal
				call		wait100us		;espera 100 useg
	;;; antes ;;;	call	wait10ms	;espero 10 mSeg
				dec		c		;05
				dec		c		;04
				call		wdt		;
				out		(c),a		;disparo
				dec		c		;03

rux12bmp02			in		a,(c)		;leio status
				and		a,$01		;
				jp		nz,rux12bmp02 ;

				inc		c		;04
				in		a,(c)		;leio +sig
				ld		d,a		;
				srl		a		;
				srl		a		;
				srl		a		;
				srl		a		;
				ld		(ix),a		;escrevo
				inc		ix		;
				ld		a,d		;
				sla		a		;
				sla		a		;
				sla		a		;
				sla		a		;
				ld		d,a		;
				inc		c		;05
				in		a,(c)		;leio -sig
				srl		a		;
				srl		a		;
				srl		a		;
				srl		a		;
				or		d		;
				ld		(ix),a		;escrevo
				inc		ix		;
				pop		bc		;c=06 e b=ultimo canal
				ld		a,b		;
				and		a,$0f		;
				sla		a		;
				ld		d,a		;salvo
				ld		a,b		;
				and		$f0		;
				sub		$40		;
				or		d		;novo canal
				ld		b,a		;b=novo canal
				ld		a,d		;
				cp		a,$10		;todo canais ja' lidos
				jp		nz,rux12bmp01 ;proximo canal
				in		a,(c)		;leio status
				ld		(ix),a		;salvo
				inc		ix		;
				pop		bc		;recupero
				inc		iy
				inc		iy
				inc		iy
				ret

	;-------------------------------------------------
	;Escrita na placa - iy
	;-------------------------------------------------
rux12bmp1			ld		a, (iy)		;recebo a prioridade da placa
				ld		(ix), a		;e a coloco no buffer de leitura
				ld		a, (iy+1)		;tambem para flag
				ld		(ix+1), a
				ld		a,(iy+2)		;
				out		(c),a		;escrevo saida digital (c=00)
				jp		rux12bmp2

	;-------------------------------------------------
	;rotina desloca iy e ix para a proxima placa
	;-------------------------------------------------
rux12bmp2			ld		bc, 11
				add		ix, bc
				ld		bc, 3
				add		iy,bc
				ret		;retorno

	;-------------------------------------------------
	;rotina especial de boot - iy / ix
	;-------------------------------------------------
rux12bmp3			ld		(iy+0),$01		;prioridade-00
				ld		(iy+1),$80		;flag-$80
				ld		(iy+2),$00		;controle-02

				ld		(ix),$01		;prioridade-00

				ld		a,(tam_in)		;
				add		a,11		;
				ld		(tam_in),a		;
				ld		a,(tam_out)		;
				add		a,3		;
				ld		(tam_out),a		;

				jp		rux12bmp2

	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;RUX12BBP - locomux 12 bits bipolar rapida
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

rux12bbp			jp		rux12bmp		;rotinas sao equivalentes

	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;MUX MICROCONTROLADA
	;MUX16BBP - locomux 16 bits bipolar rapida, microcontrolada
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

mux16bbp			ld		a,(base)
				ld		c,a
				ld		a,(tipo_opr)		;tipo de operacao
				cp		OPR_LEITURA		;
				jp		z,mux16bbp0		;leitura
				cp		OPR_ESCRITA		;
				jp		z,mux16bbp1		;escrita (= MUX)
				cp		OPR_PROXIMA		;
				jp		z,mux16bbp2		;passa para a proxima placa (= MUX)
				cp		OPR_BOOT		;
				jp		z,mux16bbp3		;boot (= MUX)
				ret		;operacao nao definida

	;-------------------------------------------------
	;Leitura da placa - ix
	;-------------------------------------------------
mux16bbp0:			ld		d, c		; salva end. base
				inc		c
				inc		c		;
				out		(c), a		; end.+2: sel. loco req
				inc		c
				call		wdt
mux16bbp01:			in		a,(c)		; leio status em end.+3
				and		a,$01		;
				jp		z,mux16bbp01 ; esta' pronta!
				ld		e, c		; salvo end.base+3

				ld		c, d		;recupero o end. base
				inc		c
				out		(c), a		; end.+1: zera ram
				dec		c

				in		a, (c)		;pula RAM (modo)
				in		a, (c)		;a:= num. integ.
				ld		(ix+22), a
				in		a, (c)		;pula RAM (num. interv)
				in		a, (c)		;pula RAM (dur. interv)

				;-------- copia da ram --------
				ld		b, 20
				push		ix
				pop		hl
				inc		hl
				inc		hl
				inir		; copia os 20 bytes da porta end.base (ram)
				; e coloca a partir de HL (igual a IX+2)

				ld		c, e		; end.base + 3
				out		(c), a		; clear loco req
				jp		mux16bbp2

	;-------------------------------------------------
	;Escrita na placa - iy
	;-------------------------------------------------
mux16bbp1			ld		a, (iy)		;recebo a prioridade da placa
				ld		(ix), a		;e a coloco no buffer de leitura
				ld		a, (iy+1)		;tambem para flag
				ld		(ix+1), a
				ld		a, (iy+2)		;consiste o modo de operacao,
				cp		a, 0		;que so' pode ser 0 ou 1,
				;ou fixado (10h, 20h, 30h, 40h)
				jp		z, mux16bbp101
				cp		a, 1
				jp		z, mux16bbp101
				cp		a, $10
				jp		z, mux16bbp101
				cp		a, $20
				jp		z, mux16bbp101
				cp		a, $30
				jp		z, mux16bbp101
				cp		a, $40
				jp		z, mux16bbp101
				xor		a		;altera o valor escrito
				ld		(iy+2), a		;para o valor default (0)

mux16bbp101:			ld		a, (iy+3)		;consiste o num. integracoes
				cp		a, 0		;que deve ser maior que zero
				jp		nz, mux16bbp102
				ld		a, 1		;altera o valor escrito para
				ld		(iy+3), a		;o valor minimo (1)

mux16bbp102:			ld		a, (iy+4)		;consiste o num. de intervalos
				cp		a, 0		;que deve ser maior que zero
				jp		nz, mux16bbp103
				ld		a, 1		;altera o valor escrito para
				ld		(iy+4), a		;o valor minimo (1)

mux16bbp103:			ld		a, (iy+5)		;duracao do intervalo
				ld		(iy+5), a		;pode ser zero, inclusive

				ld		d, c		; salvo end.base
				inc		c
				inc		c		;
				out		(c), a		; end.+2: sel. loco req
				inc		c
				call		wdt

mux16bbp11:			in		a,(c)		; leio status em end.+3
				and		a,$01		;
				jp		z,mux16bbp11 ; esta' pronta!
				ld		e, c		; salvo end.base+3

				ld		c, d		; recupera end.base
				inc		c
				out		(c), a		; zera RAM em end.base+1
				; ram esta' selecionada

				dec		c		; recupera end.base
				push		iy
				pop		hl
				inc		hl
				inc		hl		; copia a partir de IY+2
				ld		b, 4		; os 4 bytes de parametro para a RAM
				otir

				;--------- libera RAM
				ld		c, e
				out		(c), a		; libera loco req
				jp		mux16bbp2

	;-------------------------------------------------
	;rotina desloca iy e ix para a proxima placa
	;-------------------------------------------------
mux16bbp2			ld		bc, 23
				add		ix, bc
				ld		bc, 6
				add		iy,bc
				ret		;retorno

	;-------------------------------------------------
	;rotina especial de boot - iy / ix
	;-------------------------------------------------
mux16bbp3			ld		(iy+0),$01		;prioridade-00
				ld		(iy+1),$80		;flag-$80
				ld		(iy+2),$00		;modo: 0
				ld		(iy+3),$40		;integ: 64
				ld		(iy+4),$00		;num. interv.: 0
				ld		(iy+5),$00		;dur. interv.: 0

				ld		(ix),$01		;prioridade-00

				ld		a,(tam_in)		;
				add		a,23		;
				ld		(tam_in),a		;
				ld		a,(tam_out)		;
				add		a,6		;
				ld		(tam_out),a		;

				jp		mux16bbp2



	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;digiloco para atraso (booster)
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

digboo				ld		a,(base)
				ld		c,a
				ld		a,(tipo_opr)		;tipo de operacao
				cp		OPR_LEITURA		;
				jp		z,digboo2		;nao ha leitura
				cp		OPR_ESCRITA		;
				jp		z,digboo1		;escrita
				cp		OPR_PROXIMA		;
				jp		z,digboo2		;passa para a proxima placa
				cp		OPR_BOOT
				jp		z,digboo3		;boot especial para digiloco atraso
				ret		;senao, operacao invalida!

	;-------------------------------------------------
	;Escrita na placa - iy
	;-------------------------------------------------
digboo1				ld		a, (iy)		;pego a prioridade da placa
				ld		(ix), a		;e coloco na tabela de leitura
				ld		a, (iy+1)		;tambem para o flag
				ld		(ix+1), a

				ld		a, (iy+2)
				out		(c),a		;porta A (end. base)
				inc		c
				ld		a, (iy+3)
				out		(c),a		;porta B
				inc		c
				ld		a, (iy+4)
				out		(c),a		;porta C

				xor		a
				ld		(ix+2), a		;indica zero como config. (todas saida)

	;-------------------------------------------------
	;rotina desloca iy e ix para a proxima placa
	;-------------------------------------------------
digboo2				ld		bc, 6
				add		iy,bc
				inc		ix
				inc		ix
				inc		ix
				ret		;retorno


	;-------------------------------------------------
	;rotina especial de boot - iy / ix
	; faz simplesmente porta A = porta B = $FF e porta C = $00 no boot
	;-------------------------------------------------
digboo3				ld		(iy),$01		;prioridade-00
				ld		(iy+1),$80		;flag-$80
				ld		(ix),$01		;
				ld		(ix+1),$01		;
				ld		(iy+2),$FF		;porta A
				ld		(iy+3),$FF		;porta B
				ld		(iy+4),$00		;porta C e' saida
				xor		a		;guarda a conf. que a, b, c sao saida
				ld		(iy+5),d		;
				ld		(ix+2), a		;
				ld		a,(tam_out)		;
				add		a,6		;
				ld		(tam_out),a		;
				ld		a,(tam_in)		;
				add		a,3		;
				ld		(tam_in),a		;
				jp		digboo2


	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;digiloco sem opto (direta) #jrp
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

dig24cfd			ld		a,(base)
				ld		c,a
				ld		a,(tipo_opr)		;tipo de operacao
				cp		OPR_LEITURA		;
				jp		z,dig24cfd0		;leitura
				cp		OPR_ESCRITA		;
				jp		z,dig24cfd1		;escrita
				cp		OPR_PROXIMA		;
				jp		z,dig24cfd2		;passa para a proxima placa
				cp		OPR_BOOT
				jp		z,dig24cfd3		;boot
				ret		;senao, operacao invalida!
	;-------------------------------------------------
	;Leitura da placa - ix
	;-------------------------------------------------
dig24cfd0			inc		iy		;vai para o fim da pos. correspon-
				inc		iy		;dente na tabela de escrita
				inc		iy		;
				inc		iy		;
				inc		iy		;
				inc		iy		;
				inc		ix		;prioridade
				inc		ix		;flags
				ld		d, c		;salvo end. base
				inc		c		;01
				inc		c		;02
				inc		c		;03
				inc		c		;04
				in		a, (c)		;config. das portas
				ld		e, a		;salvo config.

	; configuracao das portas
	;	no end. base + 4:	X 0 0 d4 d3 0 d1 d0
	;			|	|----+----> se = 1, porta C in; se = 0, C out
	;			|	|----> se = 1, porta B in; se = 0, B out
	;			|------------> se = 1, porta A in; se = 0, A out

				ld		c, d		;end. base
				and		$10		;
				jp		z, dig24cfd0B ;se porta A e' saida
				in		a, (c)		;A e' entrada
				ld		(ix), a		;
				inc		ix		;proxima
				dec		iy		;um byte a menos na tabela de escrita
dig24cfd0b			inc		c		;01
				ld		a, e		;
				and		$02		;
				jp		z, dig24cfd0C ;se porta B e' saida
				in		a, (c)		;B e' entrada
				ld		(ix), a		;
				inc		ix		;
				dec		iy		;um byte a menos na tabela de escrita
dig24cfd0c			inc		c		;02
				ld		a, e		;
				and		$08		;
				jp		z, dig24cfd0D ;se porta C e' saida
				in		a, (c)		;C e' entrada
				ld		(ix), a		;
				inc		ix		;
				dec		iy		;um byte a menos na tabela de escrita
dig24cfd0d			ld		a, e		;coloca o byte de config.
				ld		(ix), a		;
				inc		ix		;
				ret		;

	;-------------------------------------------------
	;Escrita na placa - iy
	;-------------------------------------------------
dig24cfd1			ld		a, (iy)		;pego a prioridade da placa
				ld		(ix), a		;e coloco na tabela de leitura
				inc		iy
				inc		ix
				ld		a, (iy)		;tambem para o flag
				ld		(ix), a
				inc		iy
				inc		ix

				inc		ix
				inc		ix
				inc		ix
				inc		ix
				ld		d, c		;salvo end. base
				inc		c		;01
				inc		c		;02
				inc		c		;03
				inc		c		;04
				in		a, (c)		;config. das portas
				out		(c),a		;configuro pio sempre (dig.v2.0)
				ld		e, a		;salvo config.
	; configuracao das portas
	;	no end. base + 4:	X 0 0 d4 d3 0 d1 d0
	;			|	|----+---> se = 1, porta C in; se = 0, C out
	;			|	|---> se = 1, porta B in; se = 0, B out
	;			|-----------> se = 1, porta A in; se = 0, A out
				ld		c, d		;end. base
				and		$10		;
				jp		nz,dig24cfd1B ;se porta A e' entrada
				ld		a, (iy)		;A e' saida
				out		(c), a		;
				inc		iy		;
				dec		ix		;
				jp		dig24cfd1bb		;
dig24cfd1b			ld		a,$ff		;
				out		(c),a		;para placa dig.v3.0
dig24cfd1bb			inc		c		;01
				ld		a, e		;
				and		$02		;
				jp		nz,dig24cfd1C ;se porta B e' entrada
				ld		a, (iy)		;B e' saida
				out		(c), a		;
				inc		iy		;
				dec		ix		;
				jp		dig24cfd1cc		;
dig24cfd1c			ld		a,$ff		;
				out		(c),a		;para placa dig.v3.0
dig24cfd1cc			inc		c		;02
				ld		a, e		;
				and		$08		;
				jp		nz,dig24cfd11 ;se porta C e' entrada
				ld		a, (iy)		;C e' saida
				out		(c), a		;
				inc		iy		;
				dec		ix		;
				jp		dig24cfd12		;
dig24cfd11			ld		a,$ff		;
				out		(c),a		;
dig24cfd12			inc		iy		;pula o byte configuracao
				ret

	;-------------------------------------------------
	;rotina desloca iy e ix para a proxima placa
	;cada ponteiro sera' deslocado de 3 a 6 bytes,
	;dependendo da conf. das portas
	;-------------------------------------------------
dig24cfd2			inc		iy		;prioridade
				inc		iy
				inc		ix		;flag
				inc		ix
				inc		c		;01
				inc		c		;02
				inc		c		;03
				inc		c		;04
				in		a, (c)		;config. das portas

	; configuracao das portas
	;	no end. base + 4:	X 0 0 d4 d3 0 d1 d0
	;			|	|----+--|-> se = 1, porta C in; se = 0, C out
	;			|	|----> se = 1, porta B in; se = 0, B out
	;			|------------> se = 1, porta A in; se = 0, A out

				and		$10		;
				jp		z, dig24cfd2As ;desvia se porta A e' saida
				inc		ix		;A e' entrada, entao pula o byte
				jp		dig24cfd2B
dig24cfd2As			inc		iy		;pula o byte de saida

dig24cfd2B			and		$02		;
				jp		z, dig24cfd2Bs ;desvia se porta B e' saida
				inc		ix		;B e' entrada, entao pula o byte
				jp		dig24cfd2C
dig24cfd2Bs			inc		iy		;pula o byte de saida

dig24cfd2C			and		$08		;
				jp		z, dig24cfd2Cs ;desvia se a porta C e' saida
				inc		ix		;C e' entrada, entao pula o byte
				jp		dig24cfd2d
dig24cfd2Cs			inc		iy		;pula o byte de saida

dig24cfd2d			inc		iy		;pula o byte de config
				inc		ix		;
				ret

	;-------------------------------------------------
	;rotina especial de boot - iy / ix
	;-------------------------------------------------
dig24cfd3			ld		(iy),$01		;prioridade-00
				inc		iy		;
				ld		(iy),$80		;flag-$80
				inc		iy
				ld		(ix),$01		;
				inc		ix		;
				ld		(ix),$01		;
				inc		ix		;
				inc		c		;01
				inc		c		;02
				inc		c		;03
				inc		c		;04
				in		a,(c)		;leio configuracao
				ld		d,a		;salvo
				ld		e,$00		;total de bytes de saida
				ld		b,$00		;total de bytes de entrada
				and		a,$10		;mascara porta a
				jp		nz,dig24cfd30 ;
				inc		e		;porta A e' saida
				ld		(iy),$00		;tam_out=tam_out + 1
				inc		iy		;
dig24cfd30			ld		a,d		;recupero conf.
				and		a,$02		;mascara porta b
				jp		nz,dig24cfd31 ;
				inc		e		;porta B e' saida
				ld		(iy),$00		;tam_out=tam_out+1
				inc		iy		;
dig24cfd31			ld		a,d		;recupero conf.
				and		a,$08		;mascara porta c
				jp		nz,dig24cfd32 ;
				inc		e		;porta C e' saida
				ld		(iy),$00		;tam_out=tam_out+1
				inc		iy		;
dig24cfd32			inc		e		;configuracao
				ld		(iy),d		;
				inc		iy		;
				inc		e		;prioridade
				inc		e		;flag

				ld		a,(tam_out)		;
				add		a,e		;
				ld		(tam_out),a		;
				ld		b,9		;total de bytes entr. + saida
dig24cfd33			dec		b		;in
				dec		e		;out
				jp		nz,dig24cfd33 ;repito ate zero
				ld		a,(tam_in)		;
				add		a,b		;
				ld		(tam_in),a		;
				dec		b		;prioridade
				dec		b		;flag
dig24cfd35			dec		b		;
				jp		z,dig24cfd34		;acabou
				inc		ix		;
				jp		dig24cfd35		;
dig24cfd34			ld		(ix), d		;
				inc		ix		;config.
				ret

	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;digiloco com opto #jrp
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
dig24cfo			jp		dig24cfd		;rotinas equivalentes

	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;digiloco para leitura dos interlocks #jrp
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

digiint				ld		a,(base)
				ld		c,a
				ld		a,(tipo_opr)		;tipo de operacao
				cp		OPR_LEITURA		;
				jp		z,digiint0		;leitura
				cp		OPR_ESCRITA		;
				jp		z,digiint1		;escrita
				cp		OPR_PROXIMA		;
				jp		z,digiint2		;passa para a proxima placa
				cp		OPR_BOOT		;
				jp		z,digiint3		;boot
				ret		;operacao invalida
	;-------------------------------------------------
	;Leitura da placa - ix
	;-------------------------------------------------
digiint0			inc		ix
				inc		ix
				ld		d, 0		;
				ld		h, c		;salvo end. base (porta A)
				inc		c		;
				inc		c		;
				ld		l, c		;salvo end. base + 2 (porta C)
digiint00			out		(c), d		;selecao (0h a 7h) na porta C
				ld		c, h		;
				in		a, (c)		;leio o estado da porta A
				ld		(ix), a		;
				inc		ix		;
				ld		c, l		;recupero end. base + 2 (p. C)
				inc		d		;prox. selecao
				ld		a, d		;
				cp		8		;
				jp		nz, digiint00 ;
				inc		iy
				inc		iy
				ret		;

	;-------------------------------------------------
	;Escrita na placa - iy
	;-------------------------------------------------
digiint1			ld		a, (iy)		;recebo a prioridade da placa
				ld		(ix), a		;e coloco no buffer de leitura
				ld		a, (iy+1)		;recebo o flag e coloco no buffer
				ld		(ix+1), a		;de leitura

				ld		a, $FF		;forco bits em 1
				out		(c), a		;end. base (porta A)
				inc		c
				out		(c), a		;end. base + 1 (porta B)
				inc		c
				out		(c), a		;end. base + 2 (porta C)
				jp		digiint2

	;-------------------------------------------------
	;rotina desloca iy e ix para a proxima placa
	;-------------------------------------------------
digiint2			inc		iy		;prioridade
				inc		iy		;flag
				ld		bc, 10
				add		ix, bc
				ret		;retorno

	;-------------------------------------------------
	;rotina especial de boot - iy / ix
	;-------------------------------------------------
digiint3			ld		a,$ff		;
				out		(c),a		;porta a
				inc		c		;01
				out		(c),a		;porta b
				inc		c		;02
				out		(c),a		;porta c
				inc		c		;03
				inc		c		;04
				in		a,(c)		;configuracao
				dec		c		;03
				out		(c),a		;programo pio
				ld		(iy+0),$01		;prioridade
				ld		(iy+1),$80		;flag-$80

				ld		(ix+0),$01		;prioridade
				ld		(ix+1),$80		;flag-$80

				ld		a,(tam_out)		;
				add		a,2		;
				ld		(tam_out),a		;
				ld		a,(tam_in)		;
				add		a,10		;
				ld		(tam_in),a		;

				jp		digiint2

	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;;;;;;;;;;;;;;;;;;; SER232 ;;;;;;;;;;;;;;;;;;;;;;
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

ser232				ld		a,(base)
				ld		c, a
				ld		a,(tipo_opr)		;tipo de operacao
				cp		OPR_LEITURA		;
				jp		z,ser232a		;leitura
				cp		OPR_ESCRITA		;
				jp		z,ser232b		;escrita
				cp		OPR_PROXIMA		;
				jp		z,ser232c		;passa para a proxima placa
				cp		OPR_BOOT		;
				jp		z,ser232d		;boot do sistema
				ret		;operacao invalida!!

	;-------------------------------
	; Leitura de Placa ix
	;-------------------------------

ser232a				call		wdt

				inc		c
				inc		c
				inc		c
				inc		c
				inc		c		;testa DSR - bit 5, base+6:
				inc		c		;		ja' esta' disponivel
				in		a, (c)		;para enviar uma nova
				and		$20		;solicitacao de leitura?
				jp		z, ser232a_nao		; nao, a solicit. foi feita e
				; esta' aguardando resposta
				; da leitura
				dec		c		; sim, pode enviar o
				dec		c		; comando de leitura
				dec		c
				dec		c
				dec		c
				dec		c

				ld		a, (pende232)		;ficou algum ajuste pendente?
				cp		0
				jp		z, ser232a0		;nao, faz leitura

				ld		a, TOTCARAC232		;sim, entao faz ajuste,
				ld		(qtcarac232), a		;desprezando caracteres que nao chegaram
				xor		a
				ld		(pende232), a		;faz constar como nao mais pendentes
				jp		ser232b		;vai ajustar

				; agora pode solicitar nova leitura

ser232a0:			call		le18		; limpa eventual resto
				push		ix

				ld		a, (nplc)
				cp		100		; e' integracao cem?
				jp		nz, ser232a1

				ld		de, MAXTENTAT232 * 8
				ld		(tentativas232), de

				ld		a, DISPLAY_Cmin		; sim
				ld		(display), a
				call		wdt
				jp		ser232a11

ser232a1:			; e' integracao dez

				cp		10		; e' integracao dez?
				jp		nz, ser232ainv		; nao

				ld		de, MAXTENTAT232
				ld		(tentativas232), de

				ld		a, DISPLAY_Dmin		; sim
				ld		(display), a
				call		wdt

ser232a11:			call		ctrlc
				ld		de, STR232_READ
				call		envia_str_232
				call		wait100ms

				xor		a
				ld		(qtcarac232), a		; inicializa contador
				inc		ix
				inc		ix
				ld		(pontoIX232), ix
				ld		ix, leit232
				ld		(respIX232), ix		;pos. inicial leit.
				ld		a, 1
				ld		(ix), a
				ld		a, INT_LEI232		;servico da interrupcao
				ld		(interrupt), a

				pop		ix
				call		wdt

ser232a_nao:			call		ser232c		;prox. placa

				ld		de, (tentou232)
				inc		de
				ld		(tentou232), de
				ld		hl, (tentativas232)
				scf
				ccf
				sbc		hl, de

falhou232			jp		nz,naofalhou232
				nop
				jp		falhou232
naofalhou232:			ei		;habilita interrup.
				ret

ser232ainv:			call		wdt
				pop		ix		;valor do par. de integr.
				jp		ser232c		;		invalido

	;---------------------------------------------------
	; Escrita na Placa iy
	;---------------------------------------------------

ser232b				di
				call		wdt
				ld		a, (qtcarac232)
				cp		TOTCARAC232
				jp		nc, ser232bcom

				ld		a, 1		; esta' no meio do receb. de leitura
				ld		(pende232), a		; marca como pendente este ajuste
				jp		ser232c

ser232bcom:			ld		a, (iy+2)
				cp		1
				jp		z, ser232bvale		; e' reset
				cp		10
				jp		z, ser232bvale		; e' 10, resolucao = 0.00001 e ciclos integracao = 10
				cp		100
				jp		z, ser232bvale		; e' 100, resolucao = 0.000001 e ciclos integracao = 100 (min)

				ld		a, 1		; nao e' parametro valido
				ld		(iy+2),a		; entao vai equivaler a reset
				jp		nz, ser232b000


ser232bvale			; - limpa o buffer serial

				inc		c		;base +1
				xor		a
				out		(c), a		; limpa interrupcao
				inc		c
				ld		a, 1
				out		(c), a		; base + 2 = 0 (FIFO nao hab)
				call		wait100ms
				xor		a
				out		(c), a		; base + 2 = 0 (FIFO nao hab)

				dec		c
				ld		a, 1		;
				out		(c), a		; volta interrupcao
				dec		c

	; nao aceita mais prioridade para esta placa - fica definitivamente 32
	;		ld	a,(iy)		;recebo prioridade
	;		ld	(ix),a		;e coloco no buffer de leitura

				ld		a,(iy+1)		;copia flag
				ld		(ix+1),a

				call		wdt
				ld		de, STR232_SYSTREM
				call		envia_str_232
				call		wdt
				call		wait300ms		;multimeter HP34401A processa informacao

				ld		a, (nplc)		; testa se e' para
				cp		1		; fazer reset por
				jp		z, ser232breset		; motivo do cmd. boot

				ld		a, (iy+2)		; testa se na DPM
				cp		1		; reset
				jp		z, ser232breset		;
				cp		10		;		conf. 10, resolucao = 0.00001
				jp		z, ser232b10		;		e ciclos integracao = 10
				cp		100		;		conf. 100, resolucao = = 0.000003
				jp		z, ser232b100		;		e ciclos integracao = 100

				ld		a, 10		; se nao for valor
				ld		(iy+2), a		; valido, passa
				jp		ser232b10		; automaticamente a 10

ser232b100:			call		ctrlc		; e' conf. 100
				ld		a, (base)
				ld		c, a
				ld		de, STR232_CONFV100
				call		envia_str_232
				call		wdt
				call		wait300ms
				ld		a, 100
				jp		ser232bnplc

ser232b10:			call		ctrlc		;e' conf. 10
				ld		a, (base)
				ld		c, a
				ld		de, STR232_CONFV10
				call		envia_str_232
				call		wdt
				call		wait300ms
				ld		a, 10

ser232bnplc:			ld		(nplc), a		;marca ultima conf. feita

ser232b000			call		wdt
				ld		de, 0
				ld		(tentou232), de
				xor		a
				ld		(pende232), a		; nao esta' mais pendente
				jp		ser232c		; continua na rotina de deslocamento para proxima placa

ser232breset:			ld		de, 0
				nop
				ld		(tentou232), de
				ld		a, (iy+2)
				cp		10
				jp		z, ser232brst10
				cp		100
				jp		z, ser232brst10
				ld		a, 10
ser232brst10:			ld		(iy+2), a
				ld		(nplc), a
				call		ser232brst
				jp		ser232b

ser232brst:			call		le18
				ld		de, STR232_RST
				call		envia_str_232		; reinicializa
				call		wait01seg		; multimeter HP34401A processa informacao
				ld		de, STR232_SYSTREM
				call		envia_str_232
				call		wait100ms		;multimeter HP34401A processa informacao
				ret

le18:				ld		a, (base)		; limpa o buffer de entrada
				ld		c, a		; lendo 18 caracteres
				ld		b, 18
le18a:				in		a, (c)
				dec		b
				jp		nz, le18a
				ret

	;----------------------------------------------------
	; Operacao desloca iy e ix para proxima placa
	;----------------------------------------------------
ser232c				ld		bc, 3
				add		iy, bc
				ld		bc, 19
				add		ix, bc
				ret

	;----------------------------------------------------
	; rotina especial de boot - iy/ix
	;----------------------------------------------------
ser232d				call		wait01seg
				call		wait01seg

				inc		c		;base +1
				ld		a, 1
				out		(c), a		;habilita interrup.

				inc		c
				xor		a
				out		(c), a		;base + 2 = 0 (FIFO nao hab)
				ld		a, $1
				out		(c), a		;base + 2 = 0 (FIFO nao hab)
				xor		a
				out		(c), a		;base + 2 = 0 (FIFO nao hab)

				inc		c		;base +3
				ld		a,128		;seta D7
				out		(c),a
				dec		c

				dec		c		;base +1
				ld		a, $0
				out		(c),a		;escrevo MSByte do divisor considerando
				;clock =16MHz e BaudRate = 9600bps

				dec		c		;base
				ld		a,$68		;		era $0, $68 quando 9600 bps
				out		(c),a		;escrevo LSByte do divisor
				inc		c
				inc		c		;base +2
				inc		c		;base +3
				ld		a,3
				out		(c),a		;character lenght=8, stop-bit=1, parity =none
				inc		c		;base+4
				ld		a, 1
				out		(c),a		;fluxo on (DTR)
				dec		c
				dec		c
				dec		c
				dec		c		;base

				call		ctrlc		;executa destravamento da serial
				xor		a
				ld		(pende232),a

	;prioridade e' forcada a 32
				ld		(iy),32		;prioridade -00
				ld		(ix),32
				ld		(iy+1), $80 ;flag-$80


				ld		a,(tam_out)
				add		a,$03
				ld		(tam_out),a
				ld		a,(tam_in)
				add		a, 19
				ld		(tam_in),a

				ld		a, 1
				ld		(nplc), a		; indica reset
				jp		ser232b		; faz ajuste inicial

	;==========================================

ctrlc:				call		avanca
				ld		a, 8
				out		(c), a
				call		recua
				call		wait100ms
				ld		a, 3		; CTRL-C
				out		(c), a
				call		wait100ms
				call		avanca
				ld		a, 9
				out		(c), a
				call		recua
				ret

	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;		UDC 3000
	;Rotina para leitura/escrita de parametros
	;do controlador de temperatura UDC3000
	;da Honeywell.
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

udc3000:			ld		a,(base)
				ld		c, a
				ld		a,(tipo_opr)		;tipo de operacao
				cp		OPR_LEITURA		;
				jp		z,udc3000a		;leitura
				cp		OPR_ESCRITA		;
				jp		z,udc3000b		;escrita
				cp		OPR_PROXIMA		;
				jp		z,udc3000c		;passa para a proxima placa
				cp		OPR_BOOT		;
				jp		z,udc3000d		;boot do sistema
				ret		;operacao invalida!!

	;-------------------------------
	; Leitura de Placa ix
	;-------------------------------

udc3000a:
				ld		a,$31		; ASCII de '1'
				ld		(end_contr),a		; endereco do controlador 1
				call		avanca		; base + 4

udc3000a0:			ld		a,3
				out		(c),a		; faco RTS=1 E DTR=1

				call		recua		; base + 0

				ld		a,'0'
				out		(c),a		; escrevo '0'
				call		delay03ms		; delay de 3ms
				ld		a,(end_contr)		; pego endereco do controlador
				out		(c),a		; escrevo endereco do controlador
				call		delay03ms
				ld		hl,comand_lei		; aponta para comando de leitura
				ld		b,12		; escrevo ",0204,E4,18,"
				call		escreve_udc
				ld		hl, comand_122		; escrevo "122,00",13,10
				ld		b, 8
				call		escreve_udc


				call		avanca		; base + 4
				xor		a
				out		(c),a		; faco RTS=0 E DTR=0

				ld		b,12
				ld		a,(end_contr)
				cp		$32
				jp		z,udc3000a2 ;se for o segundo controlador nao
				; tem prioridade nem flag

				inc		ix		; pula prioridade
				inc		ix		; pula flag
udc3000a2:			call		dru		; verifica se dado pronto e retorna a base + 0
				in		a,(c)		; leio dado
				call		avanca		; base + 4
				dec		b
				ld		a,b
				cp		0
				jp		nz,udc3000a2		; desprezo "008040,122,"

				ld		b,19
udc3000a3:			xor		a
				ld		(time_out),a		; limpo time_out
				call		dru		; verifica se dado pronto e retorna a base + 0
				ld		a,(time_out)		; leio time_out
				cp		1
				jp		z,udc3000a30		; se time_out=1 não leio serial
				in		a,(c)
				ld		(ix),a
udc3000a30: call		avanca		; base + 4
				inc		ix
				dec		b
				ld		a,b
				cp		0
				jp		nz,udc3000a3
				call		wdt
				call		wait300ms		; delay de 300ms

				ld		a,3
				out		(c),a		; faco RTS=1 E DTR=1

				call		recua		; base + 0

				ld		a,'0'
				out		(c),a		; escrevo '0'
				call		delay03ms		; delay de 3ms
				ld		a,(end_contr)
				out		(c),a		; escrevo endereco do controlador
				call		delay03ms
				ld		hl,comand_lei		; aponta para comando de leitura
				ld		b,12		; escrevo ",0204,E4,18,"
				call		escreve_udc
				ld		hl, comand_001
				ld		b, 6
				call		escreve_udc		;"001,",13,10


				call		avanca		; base + 4
				xor		a
				out		(c),a		; faco RTS=0 E DTR=0

				ld		b,12
udc3000a5:			call		dru		; verifica se dado pronto e retorna a base + 0
				in		a,(c)		; leio dado
				call		avanca		; base + 4
				dec		b
				ld		a,b
				cp		0
				jp		nz,udc3000a5		; desprezo "008040,001,"

				ld		b,7
udc3000a6:			xor		a
				ld		(time_out),a		; limpo time_out
				call		dru		; verifica se dado pronto e retorna a base + 0
				ld		a,(time_out)		; leio time_out
				cp		1
				jp		z,udc3000a60		; se time_out=1 não leio serial
				in		a,(c)
				ld		(ix),a
udc3000a60: call		avanca		; base + 4
				inc		ix
				dec		b
				ld		a,b
				cp		0
				jp		nz,udc3000a6
				call		wdt
				call		wait300ms		; delay de 300ms

				ld		a,3
				out		(c),a		; faco RTS=1 E DTR=1

				call		recua		; base + 0

				ld		a,'0'
				out		(c),a		; escrevo '0'
				call		delay03ms		; delay de 3ms
				ld		a,(end_contr)
				out		(c),a		; escrevo endereco do controlador
				call		delay03ms

				ld		hl,comand_lei		; aponta para comando de leitura
				ld		b,12		; escrevo ",0204,E4,18,"
				call		escreve_udc

				ld		hl,comand_003		; escrevo "003,",13,10
				ld		b, 6
				call		escreve_udc

				call		avanca		; base + 4
				xor		a
				out		(c),a		; faco RTS=0 E DTR=0

				ld		b,12
udc3000a8:			call		dru		; verifica se dado pronto e retorna a base + 0
				in		a,(c)		; leio dado
				call		avanca		; base + 4
				dec		b
				ld		a,b
				cp		0
				jp		nz,udc3000a8		; desprezo "008040,003,"

				ld		b,7
udc3000a9:			xor		a
				ld		(time_out),a		; limpo time_out
				call		dru		; verifica se dado pronto e retorna a base + 0
				ld		a,(time_out)		; leio time_out
				cp		1
				jp		z,udc3000a90		; se time_out=1 nao leio serial
				in		a,(c)
				ld		(ix),a

udc3000a90: call		avanca		; base + 4
				inc		ix
				dec		b
				ld		a,b
				cp		0
				jp		nz,udc3000a9

				call		wdt
				call		wait300ms		; delay de 300ms

				ld		a,3
				out		(c),a		; faco RTS=1 E DTR=1

				call		recua		; base + 0

				ld		a,'0'
				out		(c),a		; escrevo '0'
				call		delay03ms		; delay de 3ms
				ld		a,(end_contr)
				out		(c),a		; escrevo endereco do controlador
				call		delay03ms

				ld		hl,comand_lei		; aponta para comando de leitura
				ld		b,12		; escrevo ",0204,E4,18,"
				call		escreve_udc
				ld		hl,comand_002
				ld		b, 6
				call		escreve_udc

				call		avanca		; base + 4
				xor		a
				out		(c),a		; faco RTS=0 E DTR=0

				ld		b,12
udc3000a11: call		dru		; verifica se dado pronto e retorna a base + 0
				in		a,(c)		; leio dado
				call		avanca		; base + 4
				dec		b
				ld		a,b
				cp		0
				jp		nz,udc3000a11		; desprezo "008040,002,"

				ld		b,7
udc3000a12: xor			a
				ld		(time_out),a		; limpo time_out
				call		dru		; verifica se dado pronto e retorna a base + 0
				ld		a,(time_out)		; leio time_out
				cp		1
				jp		z,udc3000a120		; se time_out=1 nao leio serial
				in		a,(c)
				ld		(ix),a

udc3000a120:			call		avanca		; base + 4
				inc		ix
				dec		b
				ld		a,b
				cp		0
				jp		nz,udc3000a12
				call		wdt
				call		wait300ms		; delay de 300ms

				ld		a,(end_contr)
				cp		$31
				jp		nz,udc3000a13
				ld		a,$32
				ld		(end_contr),a
				jp		udc3000a0

udc3000a13: ld			de,42
				add		iy,de		; prox.placa
				ret


	;---------------------------------------------------
	; Escrita na Placa iy
	;---------------------------------------------------

udc3000b:
				ld		a,$31		; ASCII de '1'
				ld		(end_contr),a		; endereco do controlador 1
				ld		a,(iy)		; prioridade
				ld		(ix),a
				inc		iy		; posiciono em flag
				inc		ix
				ld		a,(iy)		; flag
				ld		(ix),a

udc3000b0:			call		avanca		; base + 4
				ld		a,3
				out		(c),a		; faco RTS=1 e DTR=1
				call		recua		; base + 0
				ld		a,'0'
				out		(c),a		; escrevo '0'
				call		delay03ms
				ld		a,(end_contr)		;pego end. do controlador
				out		(c),a		; e escrevo
				call		delay03ms

				ld		hl,comand_escSP ; aponta para comando de escrita de SP
				ld		b,16
				call		escreve_udc		; escrevo ",0204,E5,18,039,"

				ld		b,5
				call		escreve_valor_udc		;escrevo o valor do Set Point

				ld		a,'0'
				out		(c),a		; escrevo '0'
				call		delay03ms
				ld		a,(end_contr)		; pego endereco do controlador
				out		(c),a		; e escrevo
				call		delay03ms
				ld		hl,comand_escPB ; aponta para comando de escrita de Proporcional
				ld		b,16
				call		escreve_udc		; escrevo ",0204,65,18,001,"

				ld		b,5
				call		escreve_valor_udc

				ld		a,'0'
				out		(c),a		; escrevo '0'
				call		delay03ms
				ld		a,(end_contr)		; pego endereco do controlador
				out		(c),a		; e escrevo
				call		delay03ms
				ld		hl,comand_escRSET ; aponta para comando de escrita de Integrativo

				ld		b,16
				call		escreve_udc		; escrevo ",0204,E5,18,003,"
				ld		b,5
				call		escreve_valor_udc

				ld		a,'0'
				out		(c),a		; escrevo '0'
				call		delay03ms
				ld		a,(end_contr)		; pego endereco do controlador
				out		(c),a		; e escrevo
				call		delay03ms

				ld		hl,comand_escRATE ; aponta para comando de escrita de Derivativo
				ld		b,16
				call		escreve_udc		; escrevo ",0204,65,18,002,"

				ld		b,5
				call		escreve_valor_udc

				ld		a,(end_contr)
				cp		$31
				jp		nz,udc3000b9
				ld		a,$32
				ld		(end_contr),a		; endereco do controlador 2
				jp		udc3000b0

udc3000b9:			inc		iy		;prox.placa
				ld		de,81
				add		ix,de
				ret


	;----------------------------------------------------
	; Operacao desloca iy e ix para proxima placa
	;----------------------------------------------------
udc3000c:
				ld		de,42
				add		iy,de
				ld		de,82
				add		ix,de
				ret

	;-----------------------------------------
escreve_udc:
				ld		a,(hl)
				out		(c),a
				call		delay03ms
				inc		hl
				dec		b
				ld		a,b
				cp		0
				jp		nz,escreve_udc
				ret

escreve_valor_udc:
				inc		iy
				ld		a,(iy)
				out		(c),a
				call		delay03ms
				dec		b
				ld		a,b
				cp		0
				jp		nz,escreve_valor_udc		;escreve valor do derivativo

				ld		a,','
				out		(c),a
				call		delay03ms
				ld		a,13
				out		(c),a		; CR
				call		delay03ms
				ld		a,10
				out		(c),a		; LF
				call		wdt
				call		wait300ms		; delay de 300ms
				ret

	;----------------------------------------------------
	; rotina especial de boot - iy/ix
	;----------------------------------------------------
udc3000d:			call		wdt
				ld		a,1
				ld		(num_ciclos),a		;altera o numero de ciclos de leitura para inversao
				; do ponto decimal no display para 1 ciclo.
				inc		c		;base +1
				inc		c
				inc		c		;base +3
				ld		a,128		;seta D7
				out		(c),a
				dec		c
				dec		c		;base +1
				ld		a,$0
				out		(c),a		;escrevo MSByte do divisor considerando
				;clock =16MHz e BaudRate = 19200bps
				dec		c		;base
				ld		a,$34
				out		(c),a		;escrevo LSByte do divisor
				inc		c
				inc		c		;base +2
				inc		c		;base +3
				ld		a,$1A
				out		(c),a		;character lenght=7, stop-bit=1, parity =even
				dec		c
				dec		c
				dec		c		;base
				ld		(iy),1		;prioridade -00
				ld		(iy+1),$80		;flag-$80
				ld		(ix),1		;prioridade
				ld		(ix+1),$80		;flag

				ld		a,(tam_out)
				add		a,42
				ld		(tam_out),a
				ld		a,(tam_in)
				add		a,82
				ld		(tam_in),a
				jp		udc3000c

	;data ready com time out

dru:				call		wdt
				ld		de,0
				inc		c		;base +5
dru1:				inc		de
				in		a,(c)
				and		a,1;
				jp		nz,dru2		;testo DR
				ld		a,d
				cp		$10
				jp		nz,dru1
				ld		a,1
				ld		(time_out),a

dru2:				xor		a
				dec		c		;base +4
				out		(c),a		;DTR=0
				dec		c
				dec		c
				dec		c
				dec		c		;base
				ret

	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	; Placa mAtoV (4-20mA to 0-10V)
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

mAtoV:				ld		a,(base)
				ld		c, a
				ld		a,(tipo_opr)		;tipo de operacao
				cp		OPR_LEITURA		;
				jp		z,mAtoV0		;leitura
				cp		OPR_ESCRITA		;
				jp		z,mAtoV1		;escrita
				cp		OPR_PROXIMA		;
				jp		z,mAtoV2		;passa para a proxima placa
				cp		OPR_BOOT		;
				jp		z,mAtoV3		;boot do sistema
				ret		;operacao invalida!!

	;---------------------------------------------
	;Leitura da placa - ix
	;---------------------------------------------

mAtoV0:				inc		ix		;prioridade
				inc		ix		;flags
				inc		c		;01
				inc		c		;02
				inc		c		;03
				inc		c		;04
				inc		c		;05
				inc		c		;06
				xor		a
				ld		(temp),a
mAtoV0a:			ld		a,(temp)
				out		(c),a		; seleciono canal 0
				call		wait10ms		; delay de 10ms
				dec		c		;05
				dec		c		;04
				out		(c),a		;disparo conversor
				dec		c		;03
				call		wdt		;

mAtoV0b:			in		a,(c)		;leio status
				and		a,$01		;
				jp		nz,mAtoV0b		;

				inc		c		;04
				in		a,(c)		;leio mais sig
				ld		d,a		;
				srl		a		;
				srl		a		;
				srl		a		;
				srl		a		;
				ld		(ix),a		;escrevo (salvo buffer)
				inc		ix		;
				ld		a,d		;
				sla		a		;
				sla		a		;
				sla		a		;
				sla		a		;
				ld		d,a		;
				inc		c		;05
				in		a,(c)		;leio menos sig
				srl		a		;
				srl		a		;
				srl		a		;
				srl		a		;
				or		d		;
				ld		(ix),a		;escrevo (salvo buffer)
				inc		ix		;
				inc		c		;06
				ld		a,(temp)		;pego canal atual
				inc		a
				cp		8
				jp		z,mAtoV0c		; se = 8 entao leu os 8 canais
				ld		(temp),a
				jp		mAtoV0a

mAtoV0c:			inc		iy		;prioridade
				inc		iy		;flag
				ret		;retorno

	;---------------------------------------------
	;Escrita na placa - iy
	;---------------------------------------------
mAtoV1:				ld		a, (iy)		;recebo a prioridade e coloco no
				ld		(ix), a		;buffer de leitura
				ld		a, (iy+1)		;tambem para flag
				ld		(ix+1), a
				jp		mAtoV2

	;---------------------------------------------
	;rotina desloca iy e ix para a proxima placa
	;---------------------------------------------
mAtoV2:				ld		de,18
				add		ix,de
				inc		iy
				inc		iy
				ret		;retorno

	;---------------------------------------------
	;rotina especial de boot - iy / ix
	;---------------------------------------------
mAtoV3:				ld		(iy),$01		;prioridade-00
				ld		(iy+1),$80		;flag-$80

				ld		a,(tam_out)		;
				add		a,$02		;
				ld		(tam_out),a		;
				ld		a,(tam_in)		;
				add		a,18		;
				ld		(tam_in),a		;
				jp		mAtoV2

	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;relogio p/ sincronismo #jrp
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

relogsin			ld		a,(base)
				ld		c,a
				ld		a,(tipo_opr)		;tipo de operacao
				cp		OPR_LEITURA		;
				jp		z,relogsin0		;leitura
				cp		OPR_ESCRITA		;
				jp		z,relogsin1		;escrita
				cp		OPR_PROXIMA		;
				jp		z,relogsin2		;passa para a proxima placa
				cp		OPR_BOOT		;
				jp		z,relogsin3		;boot
				ret		;operacao nao definida


	;-------------------------------------------------
	;Leitura da placa - ix
	;-------------------------------------------------
relogsin0			jp		relogsin2		;nao ha' leitura

	;-------------------------------------------------
	;Escrita na placa - iy
	;-------------------------------------------------
relogsin1			ld		a, (iy)		;pego a prioridade da placa
				ld		(ix), a		;e escrevo no buffer de leitura
				ld		a, (iy+1)		;tambem para flag
				ld		(ix+1), a		;

				ld		a, (relogboo_cego)
				cp		1
				jp		z, relogsin2

				ld		e, c		;
				inc		c		;01
				ld		a, (iy+2)		;
				cp		LIGA_RELOG		;255 = liga relogio
				jp		z, relogsin10
				cp		DESL_RELOG		;1 = desliga relogio
				jp		nz,relogsin11 ;c.c. nada faz
				inc		c		;end. base + 2
relogsin10			out		(c), a		;disparo liga ou desliga
relogsin11			ld		c, e		;end. base
				ld		a, (iy+3)		;
				out		(c), a		;taxa repeticao
				inc		c		;01
				inc		c		;02
				inc		c		;03
				inc		c		;04
				ld		a, (iy+4)		;
				out		(c), a		;ajuste grosso
				inc		c		;end. base + 5
				ld		a, (iy+5)		;
				out		(c), a		;ajuste fino
				jp		relogsin2

	;-------------------------------------------------
	;rotina desloca iy e ix para a proxima placa
	;-------------------------------------------------
relogsin2			ld		bc, 6
				add		iy, bc
				inc		ix
				inc		ix
				ret

	;-------------------------------------------------
	;rotina especial de boot - iy / ix
	;-------------------------------------------------
relogsin3			ld		(iy+0), $01		;prioridade
				ld		(iy+1),$80		;flag-$80
				ld		(iy+2),$01		;lig/desl (desligado)
				ld		(iy+3),$01		;tx rep
				ld		(iy+4),$01		;atg
				ld		(iy+5),$01		;atf

				ld		(ix+0), $01		;prioridade
				ld		(ix+1),$80		;flag-$80

				ld		a,(tam_in)
				add		a,2
				ld		(tam_in),a
				ld		a,(tam_out)
				add		a,6
				ld		(tam_out),a
				jp		relogsin2		;pula e retorna

	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;atraso p/ sincronismo #jrp (antes da introducao do booster)
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

atrassin			ld		a,(base)
				ld		c,a
				ld		a,(tipo_opr)		;tipo de operacao
				cp		OPR_LEITURA		;
				jp		z,atrassin0		;leitura
				cp		OPR_ESCRITA		;
				jp		z,atrassin1		;escrita
				cp		OPR_PROXIMA		;
				jp		z,atrassin2		;passa para a proxima placa
				cp		OPR_BOOT		;
				jp		z,atrassin3		;boot
				ret		;operacao nao definida

	;-------------------------------------------------
	;Leitura da placa - ix
	;-------------------------------------------------
atrassin0			jp		atrassin2		;nao ha' leitura

	;-------------------------------------------------
	;Escrita na placa - iy
	;-------------------------------------------------
atrassin1			ld		a, (iy)		;pego a prioridade da placa
				ld		(ix), a		;e coloco no buffer
				ld		a, (iy+1)		;tambem para flag
				ld		(ix), a		;

				ld		a, (iy+3)		;
				out		(c), a		;atraso grosso -sig.
				inc		c		;01
				ld		a, (iy+2)		;
				out		(c), a		;atraso grosso +sig.
				inc		c		;02
				ld		a, (iy+4)		;
				out		(c), a		;atraso fino
				inc		c		;03
				inc		c		;04
				ld		a, (iy+5)		;
				out		(c), a		;controle
				jp		atrassin2		;pula e retorna

	;-------------------------------------------------
	;rotina desloca iy e ix para a proxima placa
	;-------------------------------------------------
atrassin2			ld		bc, 6
				add		iy, bc
				inc		ix
				inc		ix
				ret

	;-------------------------------------------------
	;rotina especial de boot - iy / ix
	;-------------------------------------------------
atrassin3			ld		(iy+0),$01		;prioridade
				ld		(iy+1),$80		;flag-$80
				ld		(iy+2),$01		;atg +sig
				ld		(iy+3),$01		;atg -sig
				ld		(iy+4),$01		;atg fino
				ld		(iy+5),$00		;controle

				ld		(ix+0),$01		;prioridade
				ld		(ix+1),$80		;flag-$80

				ld		a,(tam_out)		;
				add		a,6		;
				ld		(tam_out),a		;
				ld		a,(tam_in)		;
				add		a,2		;
				ld		(tam_in),a		;

				jp		atrassin2

	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;relogio p/ sincronismo do booster #jrp
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

relogboo			ld		a,(base)
				ld		c,a
				ld		a,(tipo_opr)		;tipo de operacao
				cp		OPR_LEITURA		;
				jp		z,relogboo0		;leitura
				cp		OPR_ESCRITA		;
				jp		z,relogboo1		;escrita
				cp		OPR_PROXIMA		;
				jp		z,relogboo2		;passa para a proxima placa
				cp		OPR_BOOT		;
				jp		z,relogboo3		;boot
				ret		;operacao nao definida


	;-------------------------------------------------
	;Leitura da placa - ix
	;-------------------------------------------------
relogboo0			jp		relogboo2		;nao ha' leitura

	;-------------------------------------------------
	;Escrita na placa - iy
	;-------------------------------------------------
relogboo1			call		wdt
				ld		a, (iy)		;pego a prioridade da placa
				ld		(ix), a		;e escrevo no buffer de leitura
				ld		a, (iy+1)		;tambem para flag
				ld		(ix+1), a		;

				ld		e, c		;
				inc		c		;01
				ld		a, (iy+2)		;
				cp		LIGA_RELOG		;255 = liga relogio
				jp		z, relogboo10
				cp		DESL_RELOG		;1 = desliga relogio
				jp		nz,relogboo11 ;c.c. nada faz
				inc		c		;end. base + 2
relogboo10			out		(c), a		;disparo liga ou desliga
relogboo11			ld		c, e		;end. base
				ld		a, (iy+3)		;
				out		(c), a		;taxa repeticao

	;---- protege hinj/heje
				ld		a, (relogboo_cego)
				cp		1
				jp		z, salta_hinjheje

				inc		c		;01
				inc		c		;02
				inc		c		;03
				inc		c		;04
				ld		a, (iy+4)		;
				out		(c), a		;HINJ (era: ajuste grosso)
				inc		c		;end. base + 5
				ld		a, (iy+5)		;
				out		(c), a		;HEJE (era: ajuste fino)

salta_hinjheje:
				ld		a, (iy+6)		;
				cp		127		;
				jp		nz, relogboo110

	;############## parametro byte 7: 127 = habilitacao de deteccao ciclica
				ld		a, 1
				ld		(rampa_ciclica), a		;sinaliza a hab. ciclica
				jp		relogboonor		;		e faz hab. normal


relogboo110:			cp		255		;habilita deteccao de corrente?
				jp		nz, relogboo111

	;############## parametro byte 7: 255 = habilitacao de deteccao de corrente	###########

relogboonor:			ld		a, (relogboo_cego)
				cp		1
				jp		z, relogboo113

				ld		a, 1
				ld		(em_deteccao), a		;sinaliza deteccao
				ld		(relogboo_cego), a ;deixa a placa cega para novos ajustes

				ld		a, DISPLAY_IGUAL
				ld		(display), a
				ld		(ix+2), STBOO_CORHABILITADA		; indica o estado

				ld		a, (iy+7)
				ld		(corrlimite+1), a		;guarda o valor da corrente limite
				;para inicio da rampa do booster
				ld		a, (iy+8)
				ld		(corrlimite), a

				ld		a, (iy+9)
				ld		(delayDCCT+2), a		;guarda o valor de delay para inspecao
				;do DCCT
				ld		a, (iy+10)
				ld		(delayDCCT+1), a

				ld		a, (iy+11)
				ld		(delayDCCT), a

				push		hl
				push		iy		;guarda iy inicial

				push		iy
				pop		hl		;faz hl = iy
				ld		bc, 12
				add		hl, bc		;faz hl = iy+12

				ld		de, tabvelocBOO		;inicio da tabela
				ld		bc, 40		;copiara' 40 bytes
				ldir		;copia o bloco de (hl) para (de)

				pop		iy		;recupera iy inicial
				pop		hl

				ld		a, (iy+52)
				ld		(ultpassorampaBOO+1), a		;guarda o valor do ultimo passo de rampa
				ld		a, (iy+53)
				ld		(ultpassorampaBOO), a		;guarda o valor do ultimo passo de rampa

				ld		a, (iy+54)
				ld		(passoEJECrampa+1), a		;guarda o valor do passo de ejecao
				ld		a, (iy+55)
				ld		(passoEJECrampa), a		;guarda o valor do passo de ejecao

				ld		a, (endbase_fotsinc)
				add		a, 3
				ld		c, a
				call		zera_contboo

				ld		a, INT_WAITDCCT
				ld		(interrupt), a

				jp		relogboo113

relogboo111			cp		0
				jp		nz, relogboo112
	;############## parametro byte 7: 0 = desabilitacao de deteccao de corrente	###########
				di
				ld		a, (endbase_fotsinc)
				add		a, 3
				ld		c, a
				call		desl_contboo

				xor		a
				ld		(rampa_ciclica), a ; nao esta mais em rampa ciclica
				ld		(em_deteccao), a		;sinaliza nao mais em deteccao
				ld		(relogboo_cego), a

				ld		(interrupt), a
				ld		(display_faixa), a
				ld		(ix+2), STBOO_CORDESABILITADA ; indica o estado desabilitado

				ld		a, (buf_in)		;mostra novamente a prioridade
				cpl
				ld		(display), a		;da primeira placa

	;############## parametro byte 7: 128 = desabilitacao de deteccao de corrente	###########

relogboo112			cp		128
				jp		nz, relogboo113
				xor		a
				ld		(rampa_ciclica), a ; nao esta mais em rampa ciclica
				ld		(em_deteccao), a		;sinaliza nao mais em deteccao

relogboo113			ld		a, (iy+56)
				ld		(atraso_ciclos), a		;guarda o valor do atraso entre ciclos
				ld		a, (iy+57)
				ld		(atraso_espalha), a		;guarda o valor do atraso para ATRASBOO
				ld		a, (iy+58)
				ld		(atraso_espalha+1), a		;guarda o valor do atraso para ATRASBOO


				ld		a, BLOCO_ENTRADA ;
				ld		c, CMD_NORMAL
				call		Sinaliza		;limpo o byte de comando de ajuste

				ld		a, (em_deteccao)
				cp		1
				jp		nz, relogboo2

				ei		;habilita interrupcao se em deteccao


				jp		relogboo2		;desloca e retorna

	;-------------------------------------------------
	;rotina desloca iy e ix para a proxima placa
	;-------------------------------------------------
relogboo2			ld		bc, 59
				add		iy, bc
				inc		ix
				inc		ix
				inc		ix
				ret

	;-------------------------------------------------
	;rotina especial de boot - iy / ix
	;-------------------------------------------------
relogboo3			ld		(iy+0),$01		;prioridade
				ld		(iy+1),$80		;flag-$80
				ld		(iy+2),$01		;lig/desl (desligado)
				ld		(iy+3),$01		;tx rep
				ld		(iy+4),$00		;HINJ
				ld		(iy+5),$00		;HEJE
				ld		(iy+6),$01		;desliga deteccao de corrente
				ld		(iy+7),$FF		;corrente para injecao (+sig)
				ld		(iy+8),$FF		;corrente para injecao (-sig)
				ld		(iy+9),$0		;pulsos de 125ns para leitura DCCT (++sig)
				ld		(iy+10),$0		;pulsos de 125ns para leitura DCCT (+-sig)
				ld		(iy+11),$1		;pulsos de 125ns para leitura DCCT (--sig)

				push		iy
				ld		bc, 12		;inicio da tabela
				add		iy, bc

				ld		bc, 4		;bytes preenchidos em cada iteracao
				xor		a		;A:= 0

initabveloc			ld		(iy+0), 0		;#passos (+sig)
				ld		(iy+1), 1		;#passos (-sig)
				ld		(iy+2), 0		;taxa repet. (+sig)
				ld		(iy+3), 1		;taxa repet. (-sig)
				add		iy, bc		;iy:= iy + 4
				inc		a
				cp		10		;10 velocidades de 4 bytes
				jp		nz, initabveloc

				pop		iy		;recupera iy inicial

				ld		(iy+52), $7f		;ult. ponto a ser
				ld		(iy+53), $ff		;		executado na rampa (4095)
				ld		(iy+54), $7f		;passo
				ld		(iy+55), $ff		;		de ejecao (4095)

				ld		(ix),$01		;prioridade
				ld		(ix+1),$80		;flag-$80
				ld		(ix+2), STBOO_CORDESABILITADA		;indica estado

				ld		(ix_relogboo), ix		;salva ix para placa relogboo

				ld		a,(tam_in)		;
				add		a,3		;
				ld		(tam_in),a		;
				ld		a,(tam_out)		;
				add		a,59		;
				ld		(tam_out),a		;

				jp		relogboo2

	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;atraso p/ sincronismo #jrp (apos a introducao do BOOSTER)
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
atrasboo			ld		a,(base)
				ld		c,a
				ld		a,(tipo_opr)		;tipo de operacao
				cp		OPR_LEITURA		;
				jp		z,atrasboo0		;leitura
				cp		OPR_ESCRITA		;
				jp		z,atrasboo1		;escrita
				cp		OPR_PROXIMA		;
				jp		z,atrasboo2		;passa para a proxima placa
				cp		OPR_BOOT		;
				jp		z,atrasboo3		;boot
				ret		;operacao nao definida

	;-------------------------------------------------
	;Leitura da placa - ix
	;-------------------------------------------------
atrasboo0			jp		atrasboo2		;nao ha' leitura

	;-------------------------------------------------
	;Escrita na placa - iy
	;-------------------------------------------------
atrasboo1			ld		a, (iy)		;pego a prioridade da placa
				ld		(ix), a		;e coloco no buffer
				ld		a, (iy+1)		;tambem para flag
				ld		(ix+1), a		;

				ld		a, (iy+3)		;
				out		(c), a		;base: atraso grosso -sig. >< canal 1
				inc		c		;01
				ld		a, (iy+2)		;
				out		(c), a		;base+1: atraso grosso +sig. >< canal 1
				inc		c		;02
				ld		a, (iy+4)		;
				out		(c), a		;base+2: atraso fino >< canal 1
				inc		c		;03
				ld		a, (iy+5)		;
				out		(c), a		;base+3: controle		>< canal 1
				inc		c		;04

				ld		a, (iy+7)		;
				out		(c), a		;base+ 4: atraso grosso -sig. >< canal 2
				inc		c		;05
				ld		a, (iy+6)		;
				out		(c), a		;base+5: atraso grosso +sig. >< canal 2
				inc		c		;06
				ld		a, (iy+8)		;
				out		(c), a		;base+6: atraso fino >< canal 2
				inc		c		;07
				ld		a, (iy+9)		;
				out		(c), a		;base+3: controle		>< canal 1
				jp		atrasboo2		;pula e retorna

	;-------------------------------------------------
	;rotina desloca iy e ix para a proxima placa
	;-------------------------------------------------
atrasboo2			ld		bc, 10
				add		iy, bc
				inc		ix
				inc		ix
				ret

	;-------------------------------------------------
	;rotina especial de boot - iy / ix
	;-------------------------------------------------
atrasboo3			ld		(iy+0),$01		;prioridade
				ld		(iy+1),$80		;flag-$80
				ld		(iy+2),$01		;atg +sig		>< canal 1
				ld		(iy+3),$01		;atg -sig		>< canal 1
				ld		(iy+4),$01		;atg fino		>< canal 1
				ld		(iy+5),$00		;controle		>< canal 1
				ld		(iy+6),$01		;atg +sig		>< canal 2
				ld		(iy+7),$01		;atg -sig		>< canal 2
				ld		(iy+8),$01		;atg fino		>< canal 2
				ld		(iy+9),$00		;controle		>< canal 2

				ld		(ix),$01		;prioridade
				ld		(ix+1),$80		;flag-$80

				ld		a,(tam_out)		;
				add		a,10		;
				ld		(tam_out),a		;
				ld		a,(tam_in)		;
				add		a,2		;
				ld		(tam_in),a		;

				jp		atrasboo2

	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;Placa contadora
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

contador			ld		a,(base)
				ld		c,a
				ld		a,(tipo_opr)		;tipo de operacao
				cp		OPR_LEITURA		;
				jp		z,contador0		;leitura
				cp		OPR_ESCRITA		;
				jp		z,contador1		;escrita
				cp		OPR_PROXIMA		;
				jp		z,contador2		;passa para a proxima placa
				cp		OPR_BOOT		;
				jp		z,contador3		;boot
				ret		;operacao nao definida

	;-------------------------------------------------
	;Leitura da placa - ix
	;-------------------------------------------------
contador0			inc		c
				inc		c
				in		a,(c)		;end. base + 2: ++sig.
				ld		(ix+2), a		;
				dec		c
				in		a,(c)		;end. base + 1: +-sig.
				ld		(ix+3), a		;
				dec		c
				in		a,(c)		;end. base: --sig.
				ld		(ix+4), a		;
				jp		contador2		;retorno

	;---------------------------------------------
	;Escrita na placa - iy
	;---------------------------------------------
contador1			ld		a, (iy)		;recebo a prioridade e coloco no
				ld		(ix), a		;buffer de leitura
				ld		a, (iy+1)		;tambem para flag
				ld		(ix+1), a

				ld		a, (iy+2)		;pega o codigo de comando
				cp		0		;0 = para contador
				jp		z, contador10
				cp		255		;255 = dispara contador
				jp		z, contador15
				cp		1		;1 = para contador
				jp		nz,contadorf		;c.c. nada faz

				out		(c), a		;end. base: para contador
				jp		contadorf

contador10			inc		c		;
				inc		c		;end. base + 2: zera contador
				out		(c), a
				jp		contadorf

contador15			inc		c
				out		(c), a		;end. base + 1: dispara

contadorf			jp		contador2		;retorno

	;-------------------------------------------------
	;rotina desloca iy e ix para a proxima placa
	;-------------------------------------------------
contador2			ld		bc, 5
				add		ix, bc
				ld		bc, 3
				add		iy, bc
				ret

	;-------------------------------------------------
	;rotina especial de boot - iy / ix
	;-------------------------------------------------
contador3			ld		(iy+0),$01		;prioridade
				ld		(iy+1),$80		;flag-$80
				ld		(iy+2),$01		;lig/desl (desligado)

				ld		(ix+0),$01		;prioridade
				ld		(ix+1),$80		;flag-$80
				ld		(ix+2),$00		;contador ++sig.
				ld		(ix+3),$00		;contador +-sig.
				ld		(ix+4),$00		;contador --sig.

				ld		a,(tam_in)		;
				add		a,5		;
				ld		(tam_in),a		;
				ld		a,(tam_out)		;
				add		a,3		;
				ld		(tam_out),a		;
				jp		contador2

	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	; placa de acionamento por transistor
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

aciotran			ld		a,(base)
				ld		c,a
				ld		a,(tipo_opr)		;tipo de operacao
				cp		OPR_LEITURA
				jp		z,aciotran0		;leitura
				cp		OPR_ESCRITA
				jp		z,aciotran1		;escrita
				cp		OPR_PROXIMA
				jp		z,aciotran2		;proxima
				cp		OPR_BOOT
				jp		z,aciotran3		;boot
				ret		;operacao invalida

	;;;;;;; leitura de placa ;;;;;;;;;;

aciotran0			jp		aciotran2

	;;;;;;; escrita na placa ;;;;;;;;;;

aciotran1			ld		a, (iy)		;recebo a prioridade e coloco no
				ld		(ix), a		;buffer de leitura
				ld		a, (iy+1)		;tambem para flag
				ld		(ix+1), a
				ld		a,(iy+2)		;leio o dado do buffer
				out		(c),a		;e escrevo na saida
				jp		aciotran2


	;;;;;;;;;; proxima placa ;;;;;;;;;;;;;;;;;;

aciotran2			ld		bc, 3
				add		ix, bc
				ld		bc, 6
				add		iy, bc
				ret

	;;;;;;;;;;; rotina de boot ;;;;;;;;;;;;;;;;

aciotran3			ld		(ix),$01		;prioridade-00
				ld		(iy),$01		;prioridade-00
				ld		(iy+1),$80		;flag
				ld		(iy+2),$FF		;desaciono todas as saidas (logica invertida)

				ld		a,(tam_out)		;
				add		a,$06		;
				ld		(tam_out),a		;
				ld		a,(tam_in)		;
				add		a,$03		;
				ld		(tam_in),a		;

				jp		aciotran2

	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;stat_fnt (status para fontes) #jrp
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

stat_fnt			ld		a,(base)
				ld		c,a
				ld		a,(tipo_opr)		;tipo de operacao
				cp		OPR_LEITURA		;
				jp		z,stat_fnt0		;leitura
				cp		OPR_ESCRITA		;
				jp		z,stat_fnt1		;escrita
				cp		OPR_PROXIMA		;
				jp		z,stat_fnt2		;passa para a proxima placa
				cp		OPR_BOOT		;
				jp		z,stat_fnt3		;boot
				ret		;senao, operacao invalida!


	;-------------------------------------------------
	;Leitura da placa - ix
	;-------------------------------------------------
stat_fnt0			in		a, (c)		;Le porta
				ld		(ix+2), a		;e coloca no buffer
				inc		c		;proxima porta
				in		a, (c)		;Le porta
				ld		(ix+3), a		;e coloca no buffer
				inc		c		;proxima porta
				in		a, (c)		;Le porta
				ld		(ix+4), a		;e coloca no buffer
				jp		stat_fnt2

	;-------------------------------------------------
	;Escrita na placa - iy
	;-------------------------------------------------
stat_fnt1			ld		a, (iy)		;pego a prioridade da placa
				ld		(ix), a		;e coloco na tabela de leitura
				ld		a, (iy+1)		;tambem para o flag
				ld		(ix+1), a

				ld		a,$ff		;forco bits em 1
				out		(c),a		;porta A
				inc		c		;+ $01
				out		(c),a		;porta B
				inc		c		;+ $02
				ld		a, %11011111		;exceto bit 5
				out		(c),a		;porta C

				ld		a, (iy+2)		;verifica byte de escrita
				cp		1		;e' comando de reset?
				jp		nz, stat_fnt11 ;nao

				ld		a, %11111111		;sim: levanto bit 5
				out		(c), a		;na porta C
				call		wdt
				call		wait100ms		; delay de 100ms
				ld		a, %11011111		;desligo bit de reset
				out		(c), a

stat_fnt11			ld		(iy+2), $0		;limpa byte no buffer
				jp		stat_fnt2

	;-------------------------------------------------
	;rotina desloca iy e ix para a proxima placa
	;-------------------------------------------------
stat_fnt2			ld		bc, 3
				add		iy, bc
				ld		bc, 5
				add		ix, bc
				ret

	;-------------------------------------------------
	;rotina especial de boot - iy / ix
	;-------------------------------------------------
stat_fnt3			ld		(iy+0),$01		;prioridade-00
				ld		(iy+1),$80		;flag-$80
				ld		(iy+2),$ff		;reset
				;escreve nas portas para permitir
				;a leitura
				ld		a,$ff		;forco bits em 1
				out		(c),a		;porta A
				inc		c		;+ $01
				out		(c),a		;porta B
				inc		c		;+ $02
				ld		a,%11011111		;porta C, exceto bit 5
				out		(c),a		;

				ld		(ix+0),$01		;prioridade
				ld		(ix+1),$80		;flag-$80
				ld		(ix+2),$ff		;
				ld		(ix+3),$ff		;
				ld		(ix+4),$ff		;

				ld		a,(tam_out)		;
				add		a,3		;
				ld		(tam_out),a		;
				ld		a,(tam_in)		;
				add		a,5		;
				ld		(tam_in),a		;

				jp		stat_fnt2

	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;Detetor de Posicao (BPM_X) #jrp
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

bpm_x				ld		a,(base)
				ld		c, a
				ld		a,(tipo_opr)		;tipo de operacao
				cp		OPR_LEITURA		;
				jp		z,bpm_X0		;leitura
				cp		OPR_ESCRITA		;
				jp		z,bpm_X1		;escrita
				cp		OPR_PROXIMA		;
				jp		z,bpm_X2		;passa para a proxima placa
				cp		OPR_BOOT		;
				jp		z,bpm_X3		;boot do sistema
				ret		;operacao invalida!!

	;----- rotinas de leitura da placa (BPM_X ou Y)

bpm_XX0				dec		c		;05
				dec		c		;04
				out		(c),a		;disparo conversor
				dec		c		;03
				call		wdt		;
bpm_XX01			in		a,(c)		;leio status
				and		a,$01		;
				jp		nz,bpm_XX01		;

				inc		c		;04
				in		a,(c)		;leio mais sig
				ld		d,a		;
				srl		a		;
				srl		a		;
				srl		a		;
				srl		a		;
				ld		(ix),a		;escrevo (salvo buffer)
				inc		ix		;
				ld		a,d		;
				sla		a		;
				sla		a		;
				sla		a		;
				sla		a		;
				ld		d,a		;
				inc		c		;05
				in		a,(c)		;leio menos sig
				srl		a		;
				srl		a		;
				srl		a		;
				srl		a		;
				or		d		;
				ld		(ix),a		;escrevo (salvo buffer)
				inc		ix		;
				inc		c		;06
				ret

	;----- rotinas de leitura da placa BPM_P

bpm_PP0				push		bc		;
				ld		b,%10000001		;placa nova e velha
bpm_PP01			push		bc		;vou utilizar b e c
				out		(c),b		;seleciono canal

				call		wait100us		;espero 100 useg

				dec		c		;05
				dec		c		;04
				call		wdt		;
				out		(c),a		;disparo
				dec		c		;03

bpm_PP02			in		a,(c)		;leio status
				and		a,$01		;
				jp		nz,bpm_PP02		;

				inc		c		;04
				in		a,(c)		;leio +sig
				ld		d,a		;
				srl		a		;
				srl		a		;
				srl		a		;
				srl		a		;
				ld		(ix),a		;escrevo
				inc		ix		;
				ld		a,d		;
				sla		a		;
				sla		a		;
				sla		a		;
				sla		a		;
				ld		d,a		;
				inc		c		;05
				in		a,(c)		;leio -sig
				srl		a		;
				srl		a		;
				srl		a		;
				srl		a		;
				or		d		;
				ld		(ix),a		;escrevo
				inc		ix		;
				pop		bc		;c=06 e b=ultimo canal
				ld		a,b		;
				and		a,$0f		;
				sla		a		;
				ld		d,a		;salvo
				ld		a,b		;
				and		$f0		;
				srl		a		;
				or		d		;novo canal
				ld		b,a		;b=novo canal
				ld		a,d		;
				cp		a,$10		;todo canais ja' lidos
				jp		nz, bpm_PP01		;proximo canal
				pop		bc		;recupero
				ret

	;---------------------------------------------------------------------
	;Leitura da placa - ix
	;Esta rotina e' uma excecao, pois faz ao mesmo tempo a leitura de duas
	;ou tres placas (BPM_X, BPM_Y e BPM_P).
	;---------------------------------------------------------------------
bpm_X0				inc		ix		;prioridade
				inc		ix		;flags
				inc		c		;01
				inc		c		;02
				inc		c		;03
				inc		c		;04
				inc		c		;05
				inc		c		;06

				inc		iy		;prioridade
				inc		iy		;flags

				ld		(ix_monitorx), ix		;salva ix para placa X
				ld		b, c		;salva (base + 6) da placa X
				xor		a		;em principio, nao ha'
				ld		(end_monitorp), a		;		placa BPM_P

				ld		de, 10		;DE:= 10;
				add		ix, de		;pos. inicial de dados para a
				ld		(ix_monitory), ix		;prox. placa (monitor Y)
				ld		a, c		;
				add		$10		;end. base+6 da prox. placa (Y)
				ld		(end_monitory), a		;salva (base + 6) da placa Y

				add		$0D		;end.base + 3 da placa seguinte
				ld		c, a
				in		a, (c)		;le identificacao
				srl		a
				srl		a
				cp		CODBPMP		;e' do tipo BPM_P?
				jp		nz, bpm_X00		;nao

				ld		de, 8		;sim, entao vai para o
				add		ix, de		;		inicio da placa BPM_P
				ld		a, (ix)		;
				ld		(prior_p), a		;verifica se a prioridade de
				cp		$FF		;		BPM_P e' diferente de $FF
				jp		z, bpm_X00		;nao

				ld		a, c		;sim
				add		3		;end. base + 6
				ld		(end_monitorp), a		;sim, salva end. (base+6)
				inc		ix		;		da placa BPM_P
				inc		ix		;pos. inicial de dados para a
				ld		(ix_monitorp), ix		;prox. placa (monitor P)

bpm_X00				ld		h, 4

bpm_X01				call		wdt
				ld		a, (iy)		;seleciona mascara i
				inc		iy
				ld		e, a

	;----------- faz leitura para BPM_X

				ld		ix, (ix_monitorx)		;recupera indice placa X no buffer
				ld		c, b		;recupera (base+6) da placa X
				out		(c), e		;seleciona monitor

	;tempo de chaveamento 100 ms

				call		wdt
				call		wait100ms		; delay de 100ms
				call		wdt

				call		bpm_XX0		;le placa X
				ld		(ix_monitorx), ix		;salva indice placa X no buffer
				ld		b, c		;salva (base+6) da placa X

	;----------- faz leitura para BPM_Y

				ld		a, (end_monitory)		;recupera end.base+6 placa Y
				ld		c, a
				ld		ix, (ix_monitory)		;recupera indice placa Y no buffer
				call		bpm_XX0		;le placa Y
				ld		(ix_monitory), ix		;salva indice placa Y no buffer

	;----------- se existe BPM_P, faz leitura

				ld		a, (end_monitorp)		;recupera end.base+6 placa P
				cp		0		;existe a placa P?
				jp		z, bpm_X02
				ld		c, a		;sim, pega end. base + 6
				ld		ix, (ix_monitorp)		;recupera ponteiro
				call		bpm_PP0		;faz leitura
				ld		(ix_monitorp), ix		;atualiza ponteiro
	;----------------------------------------------------------------------------
bpm_X02				dec		h		;verifica se ainda ha'
				jp		nz, bpm_X01		;		monitor a ser lido
	;----------------------------------------------------------------------------

				inc		iy		;move iy para a placa seguinte
				inc		iy		;a BPM_Y

				ld		a, (end_monitorp)		;recupera end.base+6 placa P
				cp		0		;existe a placa P?
				ret		z
				inc		iy		;sim, entao move IY para a
				inc		iy		;		placa BPM_P

				ld		a, (prior_p)		;se a prioridade for diferen-
				cp		$FF		;		te de $FF, P ja foi lida
				ret		nz		;		e IX atualizado

				ld		de, 34		;senao IX deve apontar para
				add		ix, de		;		a proxima placa
				ret		;retorno

	;---------------------------------------------
	;Escrita na placa - iy
	;---------------------------------------------
bpm_X1				ld		a, (iy)		;recebo a prioridade e coloco no
				ld		(ix), a		;buffer de leitura
				ld		a, (iy+1)		;tambem para flag
				ld		(ix+1), a

				ld		de, 6
				add		iy, de
				ld		de, 10
				add		ix, de
				ret		;retorno

	;---------------------------------------------
	;rotina desloca iy e ix para a proxima placa (tanto para BPM_X como Y - e P)
	;---------------------------------------------
bpm_X2				ld		a, c
				add		$23
				ld		c, a
				in		a, (c)
				srl		a
				srl		a
				cp		CODBPMP		;existe a placa P?
				jp		z, bpm_X20
				ld		de, 6+2		;faz para X e Y
				add		iy, de
				ld		de, 10+10
				add		ix, de
				ret

bpm_X20				ld		de, 6+2+2		;faz para X, Y e P
				add		iy, de
				ld		de, 10+10+34
				add		ix, de
				ret		;retorno

	;---------------------------------------------
	;rotina especial de boot - iy / ix
	;---------------------------------------------
bpm_X3				ld		(iy+0),$FF		;prioridade-00
				ld		(iy+1),$80		;flag-$80
				ld		(iy+2), $00
				ld		(iy+3), $00
				ld		(iy+4), $00
				ld		(iy+5), $00

				ld		(ix+0),$FF		;prioridade
				ld		(ix+1),$0
				ld		(ix+2),$0
				ld		(ix+3),$0
				ld		(ix+4),$0
				ld		(ix+5),$0
				ld		(ix+6),$0
				ld		(ix+7),$0
				ld		(ix+8),$0
				ld		(ix+9),$0

				ld		a,(tam_out)		;
				add		a, 6		;
				ld		(tam_out),a		;
				ld		a,(tam_in)		;
				add		a, 10		;
				ld		(tam_in),a		;

				ld		de, 6		;
				add		iy, de
				ld		de, 10
				add		ix, de
				ret

	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;Detetor de Posicao (BPM_Y) #jrp
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

bpm_y				ld		a,(tipo_opr)		;tipo de operacao
				cp		OPR_LEITURA		;
				ret		z		;ja leu (com BPM_X)
				cp		OPR_ESCRITA		;
				jp		z,bpm_y1		;escrita
				cp		OPR_PROXIMA		;
				ret		z		;ja pulou para a proxima (com BPM_X)
				cp		OPR_BOOT		;
				jp		z,bpm_y3		;boot do sistema
				ret		;operacao invalida!!

	; ajuste
	;---------------------------------------------------------

bpm_y1				ld		a, (iy)		;recebo a prioridade e coloco no
				ld		(ix), a		;buffer de leitura
				ld		a, (iy+1)		;tambem para flag
				ld		(ix+1), a
				jp		bpm_y2		;retorno

	; pula para a proxima placa
	;---------------------------------------------------------

bpm_y2				ld		de, 10
				add		ix, de
				inc		iy
				inc		iy
				ret		;retorno

	;---------------------------------------------
	;rotina especial de boot - iy / ix
	;---------------------------------------------
bpm_y3				ld		(iy+0),$FE		;prioridade-00
				ld		(iy+1),$80		;flag-$80

				ld		(ix+0),$FF		;prioridade
				ld		(ix+1),$0
				ld		(ix+2),$0
				ld		(ix+3),$0
				ld		(ix+4),$0
				ld		(ix+5),$0
				ld		(ix+6),$0
				ld		(ix+7),$0
				ld		(ix+8),$0
				ld		(ix+9),$0

				ld		a,(tam_out)
				add		a,2
				ld		(tam_out),a
				ld		a,(tam_in)
				add		a,10
				ld		(tam_in),a

				jp		bpm_y2

	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;Detetor de Posicao (BPM_P) #jrp
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

bpm_P				ld		a,(tipo_opr)		;tipo de operacao

				cp		OPR_LEITURA		;
				ret		z		;ja leu (com BPM_X)
				cp		OPR_ESCRITA		;
				jp		z,bpm_P1		;escrita
				cp		OPR_PROXIMA		;
				ret		z		;ja pulou para a proxima (com BPM_X)
				cp		OPR_BOOT		;
				jp		z,bpm_P3		;boot do sistema
				ret		;operacao invalida!!

	;---------------------------------------------
	;Escrita na placa - iy
	;---------------------------------------------
bpm_P1				ld		a, (iy)		;recebo a prioridade e coloco no
				ld		(ix), a		;buffer de leitura
				ld		a, (iy+1)		;tambem para flag
				ld		(ix+1), a

				ld		de, 34		;desloca para proxima
				add		ix, de
				inc		iy
				inc		iy
				ret		;retorno

	;---------------------------------------------
	;rotina especial de boot - iy / ix
	;---------------------------------------------
bpm_P3				ld		(iy),$FE		;prioridade = $FF significa que
				inc		iy		;		a placa nao e' lida
				ld		(iy),$80		;flag-$80
				inc		iy		;
				ld		(ix),$FF		;prioridade
				inc		ix		;

				ld		a, 33		;flag + 32 bytes
bpm_P31				ld		(ix),$0		;zera
				inc		ix		;
				dec		a		;chegou ao ultimo byte?
				jp		nz, bpm_P31

				ld		a,(tam_out)		;
				add		a, 2		;
				ld		(tam_out),a		;
				ld		a,(tam_in)		;
				add		a,34		;
				ld		(tam_in),a		;
				ret


	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;Placa contadora de fotons
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

contfoto			ld		a,(base)
				ld		c,a
				ld		a,(tipo_opr)		;tipo de operacao
				cp		OPR_LEITURA		;
				jp		z,contfoto0		;leitura
				cp		OPR_ESCRITA		;
				jp		z,contfoto1		;escrita
				cp		OPR_PROXIMA		;
				jp		z,contfoto2		;passa para a proxima placa
				cp		OPR_BOOT		;
				jp		z,contfoto3		;boot
				ret		;operacao nao definida

	;-------------------------------------------------
	;Leitura da placa - ix
	;-------------------------------------------------
contfoto0			ld		a, (ix+5)
				cp		a, FOT_EMCONTAGEM		;estava em contagem?

				jp		nz, contfoto01
				inc		c
				inc		c		;vai verificar se acabou
				inc		c
				in		a, (c)		;a:= (endbase+3): status
				dec		c
				bit		0, a		;bit 0 indica:
				jp		nz, contfoto02		;ja' terminou a contagem?
				ld		a, FOT_EMCONTAGEM
				ld		(iy+5), a		;nao! Entao sinaliza
				jp		contfoto2

				; parado
contfoto01			cp		a, FOT_PARA
				jp		nz, contfoto03
				ld		(ix+5), a
				inc		c
				inc		c		;end. base+2

				;concluido - faz leitura
contfoto02			in		a,(c)		;end. base + 2: ++sig.
				ld		(ix+2), a		;
				dec		c
				in		a,(c)		;end. base + 1: +-sig.
				ld		(ix+3), a		;
				dec		c
				in		a,(c)		;end. base: --sig.
				ld		(ix+4), a		;
				ld		a, (dispfotcontinuo)
				cp		1		;se estava em redisparo continuo, faz mais
				jp		nz, contfoto021		;		um redisparo de contagem
				inc		c		;zera antes de redisparar
				inc		c
				inc		c
				ld		a, 2
				out		(c), a		;(end. base+3):= 2 para zerar
				dec		c
				dec		c
				dec		c		; deixa C com o end. base
				jp		contfoto1		; vai para fazer o novo disparo

contfoto021			ld		a, FOT_PARA		; nao, era para fazer um disparo so'
				ld		(ix+5), a
				jp		contfoto2		;pula e retorna

				;em free run
contfoto03			cp		FOT_FREERUN
				jp		nz, contfoto2
				;para free run temporariamente
				inc		c
				inc		c
				inc		c
				ld		a, 1
				out		(c), a		;endbase + 3:= 1
				inc		c
				inc		c
				xor		a		;limpa flip-flop free run
				out		(c), a		;end. base+5 := 0

				;faz leitura
				dec		c
				dec		c
				dec		c
				in		a,(c)		;end. base + 2: ++sig.
				ld		(ix+2), a		;
				dec		c
				in		a,(c)		;end. base + 1: +-sig.
				ld		(ix+3), a		;
				dec		c
				in		a,(c)		;end. base: --sig.
				ld		(ix+4), a		;

				;redispara free run
				inc		c		;reDISPARA free run
				inc		c
				inc		c
				xor		a
				out		(c), a		;endbase + 3:= 0
				inc		c
				inc		c
				out		(c), a		;end. base+5 := 0
				dec		c
				dec		c
				xor		a
				inc		c
				out		(c), a		;end. base+4:= 0
				jp		contfoto2		;pula e retorna

	;---------------------------------------------
	;Escrita na placa - iy
	;---------------------------------------------
contfoto1			ld		a, (iy)		;recebo a prioridade e coloco no
				ld		(ix), a		;buffer de leitura
				ld		a, (iy+1)		;tambem para flag
				ld		(ix+1), a

				ld		a, (iy+5)		;pega o codigo de comando
				cp		FOT_ZERA		;0 = reset contador
				jp		z, contfoto10
				cp		FOT_EMCONTAGEM ;255 = dispara contador
				jp		z, contfoto1255
				cp		FOT_PARA		;1 = para free-run
				jp		z,contfoto11
				cp		FOT_FREERUN		;127 = dispara free-run
				jp		z,contfoto1127
				cp		FOT_CONTINUO
				jp		z,contfoto1192 ;192=dispara por tempo continuamente (ate' um "para free-run")
				jp		contfoto2		;c.c. nada faz

				;------------ (0) ZERA

contfoto10			inc		c		;ZERA
				inc		c
				inc		c
				ld		a, 2
				out		(c), a		;end. base + 3: zera contador
				jp		contfoto2

				;------------ (192) REDISPARA POR TEMPO, porem CONTINUAMENTE

contfoto1192			ld		a, 1
				ld		(dispfotcontinuo), a		; sinaliza que e' para redisparar ao
				; terminar a contagem

				;------------ (255) DISPARA com tempo determinado

contfoto1255			inc		c		;DISPARA
				inc		c
				inc		c
				xor		a
				out		(c), a		;end. base+3 := 0
				inc		c
				inc		c
				xor		a
				out		(c), a		;end. base+5 := 0
				dec		c
				dec		c
				dec		c
				ld		a, (iy+2)
				out		(c), a		;end. base+2 := ++sig
				dec		c
				ld		a, (iy+3)
				out		(c), a		;end. base+1 := +-sig
				dec		c
				ld		a, (iy+4)
				out		(c), a		;end. base		:= --sig
				xor		a
				inc		c
				inc		c
				inc		c
				out		(c), a		;end. base+3 := 0
				ld		a, FOT_EMCONTAGEM
				ld		(ix+5), a
				jp		contfoto2

				;------------ (127) DISPARA free run

contfoto1127			inc		c		;DISPARA free run
				inc		c
				inc		c
				inc		c
				inc		c
				xor		a
				out		(c), a		;end. base+5 := 0
				dec		c
				dec		c
				out		(c), a		;end. base+3 := 0
				inc		c
				out		(c), a		;end. base+4:= 0
				ld		a, FOT_FREERUN
				ld		(ix+5), a
				jp		contfoto2

				;------------ (1) PARA free run

contfoto11			inc		c		;PARA free run
				inc		c
				inc		c
				ld		a, 1
				out		(c), a		;end. base+3 := 1
				inc		c
				inc		c
				xor		a		;limpa flip-flop free run
				out		(c), a		;end. base+5 := 0
				ld		a, FOT_PARA
				ld		(ix+5), a
				xor		a
				ld		(dispfotcontinuo), a ; interrompe a possibilidade de redisparo
				jp		contfoto2		;proxima placa e retorno

	;-------------------------------------------------
	;rotina desloca iy e ix para a proxima placa
	;-------------------------------------------------

contfoto2			ld		bc, 6
				add		ix, bc
				add		iy, bc
				ret

	;-------------------------------------------------
	;rotina especial de boot - iy / ix
	;-------------------------------------------------
contfoto3			ld		(iy),$01		;prioridade
				ld		(iy+1),$80		;flag-$80
				ld		(iy+2),$EF		;tempo e' calculado
				ld		(iy+3),$FF		;		como F00000 - t/32us
				ld		(iy+4),$FF		;
				ld		a, FOT_ZERA
				ld		(iy+5),a		;lig/desl (desligado)

				ld		(ix),$01		;prioridade
				ld		(ix+1),$80		;flag-$80
				ld		(ix+2),$00		;contador ++sig.
				ld		(ix+3),$00		;contador +-sig.
				ld		(ix+4),$00		;contador --sig.
				ld		a, FOT_PARA
				ld		(ix+5), a		;contador --sig.

				ld		a,(tam_in)		;
				add		a,6		;
				ld		(tam_in),a		;
				ld		a,(tam_out)		;
				add		a,6		;
				ld		(tam_out),a		;
				xor		a
				ld		(dispfotcontinuo), a		;		zera a capacidade de redisparo continuo
				jp		contfoto2		;pula e retorna


	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;Placa contadora de tempo para leitura do DCCT
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

contdcct			ld		a,(base)
				ld		c,a
				ld		a,(tipo_opr)		;tipo de operacao
				cp		OPR_LEITURA		;
				jp		z,contdcct2		;nao ha leitura
				cp		OPR_ESCRITA		;
				jp		z,contdcct1		;escrita
				cp		OPR_PROXIMA		;
				jp		z,contdcct2		;passa para a proxima placa
				cp		OPR_BOOT		;
				jp		z,contdcct3		;boot
				ret		;operacao nao definida

	;-------------------------------------------------
	;Leitura da placa - ix
	;-------------------------------------------------
	;---------------------------------------------
	;Escrita na placa - iy
	;---------------------------------------------
contdcct1			ld		a, (iy)		;recebo a prioridade e coloco no
				ld		(ix), a		;buffer de leitura
				ld		a, (iy+1)		;tambem para flag
				ld		(ix+1), a
				jp		contdcct2		;proxima placa e retorno

	;-------------------------------------------------
	;rotina desloca iy e ix para a proxima placa
	;-------------------------------------------------

contdcct2			inc		ix
				inc		ix
				inc		iy
				inc		iy
				ret

	;-------------------------------------------------
	;rotina especial de boot - iy / ix
	;-------------------------------------------------
contdcct3			ld		(iy),$01		;prioridade
				ld		(iy+1),$80		;flag-$80
				ld		(ix),$01		;prioridade
				ld		(ix+1),$80		;flag-$80

				;inicializa
				inc		c
				inc		c
				inc		c
				call		zera_contboo

				ld		a,(tam_in)		;
				add		a,2		;
				ld		(tam_in),a		;
				ld		a,(tam_out)		;
				add		a,2		;
				ld		(tam_out),a		;
				jp		contdcct2		;pula e retorna


	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;Placa contadora de passos da rampa do booster
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

contboo				ld		a,(base)
				ld		c,a
				ld		a,(tipo_opr)		;tipo de operacao
				cp		OPR_LEITURA		;
				jp		z,contboo0		;leitura
				cp		OPR_ESCRITA		;
				jp		z,contboo1		;escrita
				cp		OPR_PROXIMA		;
				jp		z,contboo2		;passa para a proxima placa
				cp		OPR_BOOT		;
				jp		z,contboo3		;boot
				ret		;operacao nao definida

	;-------------------------------------------------
	;Leitura da placa - ix
	;-------------------------------------------------
contboo0			ld		a, (ix+5)
				cp		a, FOT_FREERUN		;estava em contagem?

				jp		nz, contboo01
				inc		c
				inc		c		;vai verificar se acabou
				inc		c
				in		a, (c)		;a:= (endbase+3): status
				dec		c
				dec		c
				dec		c
				bit		0, a		;bit 0 indica:
				jp		z, contboo01		;ja' terminou a contagem?
				ld		a, FOT_PARA
				ld		(iy+5), a		;sim! Entao sinaliza
				ld		(ix+5), a		;sim! Entao sinaliza


contboo01			inc		c
				inc		c
				;faz leitura
				in		a,(c)		;end. base + 2: ++sig.
				ld		(ix+2), a		;
				dec		c
				in		a,(c)		;end. base + 1: +-sig.
				ld		(ix+3), a		;
				dec		c
				in		a,(c)		;end. base: --sig.
				ld		(ix+4), a		;
				jp		contboo2		;pula e retorna


	;---------------------------------------------
	;Escrita na placa - iy
	;---------------------------------------------
contboo1			ld		a, (iy)		;recebo a prioridade e coloco no
				ld		(ix), a		;buffer de leitura
				ld		a, (iy+1)		;tambem para flag
				ld		(ix+1), a

				ld		a, (relogboo_cego)
				cp		1
				jp		z, contboo2


				ld		a, (iy+5)		;pega o codigo de comando
				cp		255		;255 = nenhuma acao
				jp		z, contboo2
				cp		0		;0 = reset contador
				jp		z, contboo10
				cp		1		;1 = para contagem
				jp		z,contboo11
				cp		127		;127 = dispara contagem
				jp		z,contboo1127
				jp		contboo2		;c.c. nada faz

				;------------ (0) ZERA

contboo10			inc		c		;ZERA
				inc		c
				inc		c
				ld		a, 2
				out		(c), a		;end. base + 3:= 2 >> zera contador
				ld		a, 255
				ld		(iy+5), a		;nada faz
				ld		a, FOT_PARA
				ld		(ix+5), a		;nada faz
				jp		contboo2

				;------------ (127) DISPARA com numero de passos determinado

contboo1127			inc		c		;DISPARA
				inc		c
				inc		c
				xor		a
				out		(c), a		;end. base+3 := 0
				inc		c
				inc		c
				out		(c), a		;end. base+5 := 0
				dec		c
				dec		c
				dec		c
				ld		a, (iy+2)
				out		(c), a		;end. base+2 := ++sig
				dec		c
				ld		a, (iy+3)
				out		(c), a		;end. base+1 := +-sig
				dec		c
				ld		a, (iy+4)
				out		(c), a		;end. base		:= --sig
				xor		a
				inc		c
				inc		c
				inc		c
				out		(c), a		;end. base+3 := 0
				inc		c
				inc		c
				out		(c), a		;end. base+5 := 0
				ld		a, FOT_FREERUN
				ld		(ix+5), a
				ld		a, 255
				ld		(iy+5), a		;nada faz
				jp		contboo2

				;------------ (1) PARA free run

contboo11			inc		c		;PARA free run
				inc		c
				inc		c
				ld		a, 5
				out		(c), a		;end. base+3:= 5
				inc		c
				inc		c
				xor		a		;limpa flip-flop free run
				out		(c), a		;end. base+5 := 0
				ld		a, FOT_PARA
				ld		(ix+5), a
				ld		a, 255
				ld		(iy+5), a		;nada faz
				jp		contboo2		;proxima placa e retorno


	;-------------------------------------------------
	;rotina desloca iy e ix para a proxima placa
	;-------------------------------------------------

contboo2			ld		bc, 6
				add		ix, bc
				add		iy, bc
				ret

	;-------------------------------------------------
	;rotina especial de boot - iy / ix
	;-------------------------------------------------
contboo3			ld		(iy),$01		;prioridade
				ld		(iy+1),$80		;flag-$80
				ld		(iy+2),$00		;contador ++sig.
				ld		(iy+3),$FF		;contador +-sig.
				ld		(iy+4),$FF		;contador --sig.
				ld		(iy+5), 255		;nada faz
				ld		(ix),$01		;prioridade
				ld		(ix+1),$80		;flag-$80
				ld		(ix+2),$00		;contador ++sig.
				ld		(ix+3),$FF		;contador +-sig.
				ld		(ix+4),$FF		;contador --sig.
				ld		(ix+5), FOT_PARA		;nada foi feito

				;inicializa
				inc		c
				inc		c
				inc		c
				call		zera_contboo

				ld		a,(tam_in)		;
				add		a,6		;
				ld		(tam_in),a		;
				ld		a,(tam_out)		;
				add		a,6		;
				ld		(tam_out),a		;
				jp		contboo2		;pula e retorna

	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;RECEBE_CAR - recebe carater via serial RS 232
	; a - carater recebido
	; c - end. base da serial
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

recebe_car			inc		c

tst_rxrdy			in		a, (c)		;end. 01
				and		%00000010		;carater disponivel?
				;(RxRdy)
				jp		z, tst_rxrdy		;
				dec		c		;end. 00
				in		a, (c)		;le o carater
				ret

	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;ENVIA_CAR - transmite carater via
	;		serial RS 232
	; b - carater
	; c - end. base da serial
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

envia_car			inc		c		;end. 01

tst_txrdy			in		a, (c)
				and		%00000101		;pode-se enviar?
				;(TxEmpty & TxRdy)
				jp		z, tst_txrdy
				dec		c		;end. 00
				out		(c), b		;envia o carater
				ret

	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;RECEBE8	- recebe e coloca no buffer oito
	;		caracteres da serial RS 232
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

recebe8				ld		d, 8		;
recebido			call		recebe_car		;aguarda carater
				ld		(ix), a		;coloca no buffer
				inc		(ix)		;
				dec		d		;conta
				ret		z		;ja chegou o ultimo?
				jp		recebido		;proximo

	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;SERVACUO - serial de vacuo #jrp
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;servacuo	ld		a,(base)
	;		ld		c,a
	;		ld		a,(tipo_opr)	;tipo de operacao
	;		cp		OPR_LEITURA	;
	;		jp		z,servacuo0	;leitura
	;		cp		OPR_ESCRITA	;
	;		jp		z,servacuo1	;escrita
	;		cp		OPR_PROXIMA	;
	;		jp		z,servacuo2	;passa para a proxima placa
	;		cp		OPR_BOOT	;
	;		jp		z,servacuo3	;boot
	;		ret			;operacao nao definida
	;
	;-------------------------------------------------
	;Leitura da placa - ix
	;-------------------------------------------------
	;servacuo0	inc	ix		;prioridade
	;		inc	ix		;flag
				;Envia comando...
				;"R1<CR>" - le canal padrao
	;		ld		b, "R"	;
	;		call	envia_car	;
	;		ld		b, "1"	;
	;		call	envia_car	;
	;		ld		b, $0D	;
	;		call	envia_car	;
	;
	;		call	recebe8	;aguarda resposta
	;
	;					;"R2<CR>" - le canal A1
	;		ld		b, "R"	;
	;		call	envia_car	;
	;		ld		b, "2"	;
	;		call	envia_car	;
	;		ld		b, $0D	;
	;		call	envia_car	;
	;
	;		call	recebe8	;aguarda resposta
	;
	;					;"R4<CR>" - le canal B1
	;		ld		b, "R"	;
	;		call	envia_car	;
	;		ld		b, "4"	;
	;		call	envia_car	;
	;		ld		b, $0D	;
	;		call	envia_car	;
	;
	;		call	recebe8	;aguarda resposta
	;
	;		%rept	5
	;		inc	iy		;proxima placa
	;		%endrept
	;		ret
	;
	;-------------------------------------------------
	;Escrita na placa - iy
	;-------------------------------------------------
	;servacuo1	ld		a, (iy)	;pego a prioridade da placa
	;		ld		(ix), a	;e coloco no buffer
	;		inc	iy
	;		inc	ix
	;		ld		a, (iy)	;tambem para flag
	;		ld		(ix), a	;
	;		inc	iy		;
	;		inc	ix
	;
	;		ld		a, 32
	;servacuo10	inc		ix
	;		dec		a
	;		jp		nz, servacuo10
	;		ret			;
	;
	;-------------------------------------------------
	;rotina desloca iy e ix para a proxima placa
	;-------------------------------------------------
	;servacuo2
	;		%rept		5
	;		inc		iy
	;		%endrept
	;		ld		a, 34
	;servacuo20	inc		ix
	;		dec		a
	;		jp		nz, servacuo20
	;		ret
	;
	;-------------------------------------------------
	;rotina especial de boot - iy / ix
	;-------------------------------------------------
	;servacuo3	inc		c		;01
	;		inc		c		;02
	;		inc		c		;03
	;		ld		a, 1
	;		out		(c), a	;reset
	;		dec		c		;02
	;		dec		c		;01
	;
	;		ld		a, $7E	;modo de operacao:
	;		out		(c), a	;9600, 8 bits, even, 1 stop bit
	;
	;		ld		a, $37	;comando:
	;		out		(c), a	;transmit enable, DTR, receive
	;					;enable, error reset, RTS
	;
	;		dec		c		;end. base
	;		ld		(iy),$01	;prioridade
	;		inc		iy		;
	;		ld		(iy),$80	;flag-$80
	;		inc		iy		;
	;		ld		(iy),$01	;asc0
	;		inc		iy		;
	;		ld		(iy),$01	;asc1
	;		inc		iy		;
	;		ld		(iy),$01	;<CR>
	;
	;		ld		(ix),$01	;prioridade
	;		inc		ix		;
	;		ld		(ix),$80	;flag-$80
	;		inc		ix		;proxima placa
	;
	;		%for kkk = 1 to 3
	;		%rept		7
	;		ld		(ix),$20	; = " "
	;		inc		ix		;proxima placa
	;		%endrept
	;		ld		(ix),$0D	; = <CR>
	;		inc		ix		;proxima placa
	;		%endfor
	;
	;		ld		a,(tam_out)	;
	;		add		a,5		;
	;		ld		(tam_out),a	;
	;		ld		a,(tam_in)	;
	;		add		a,34		;
	;		ld		(tam_in),a	;
	;		ret
	;

	;placa nao instalada
nao_inst			ret


	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;SINALIZA
	;Escreve no primeiro byte do bloco da DPM
	;dado em A o codigo de comando dado em C
	;Destroi - A
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

Sinaliza:			out		($03), a		;seleciona bloco "a" da DPM
				ld		a, c
				out		($00), a		;escreve comando no bloco
				ret

	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;INIC_MAPA
	;Inicializa o mapa de configuracao de placas
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

inic_mapa			ld		hl,mapa_placa
				ld		a, 32

prog_pri0			ld		(hl), NAO_INSTALADA		;tipo
				inc		hl
				ld		(hl), NAO_INSTALADA		;tipo estendido
				inc		hl
				%rept		3
				ld		(hl), 0		;end. base, end. desvio
				inc		hl
				%endrept
				dec		a
				cp		0
				jp		nz, prog_pri0
				ld		(hl), NAO_INSTALADA		;marca fim do mapa
				ret

	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;INIC_RAMPA
	;Inicializa o mapa de rampa
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

inic_rampa:			ld		hl,mapa_rampa
				ld		a, 32

inic_rampa1:			ld		(hl), NAO_INSTALADA		;tipo
				ld		de, TAM_MAPARAMPA
				add		hl, de
				dec		a
				cp		0
				jp		nz, inic_rampa1
				ld		(hl), NAO_INSTALADA		;marca fim do mapa

				ld		hl, buf_rampa
				ld		(ptbuf_rampa), hl
				ret


	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;INIC_CICLA
	;Inicializa o mapa de ciclagem
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

inic_cicla			ld		bc,mapa_cicla
				ld		a, 32

prog_pri2			push		af
				ld		a,NAO_CICLAVEL
				ld		(bc),a		; NAO_CICLAVEL
				inc		bc
				ld		a,NAO_INSTALADA
				ld		(bc),a		;tipo
				pop		af
				%rept 6
				inc		bc
				%endrept
				dec		a
				cp		0
				jp		nz, prog_pri2
				ld		a,NAO_CICLAVEL
				ld		(bc),a		;marca fim do mapa
				inc		bc
				ld		a,NAO_INSTALADA
				ld		(bc),a
				ret

	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;;;;;;;;;;;; MONTA O MAPA DE CICLAGEM ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

monta_tab			ld		ix,buf_in
				ld		hl,mapa_placa+5 ;pula placa serial
				ld		bc,mapa_cicla
inicio				ld		a,(hl)
				cp		a,NAO_INSTALADA
				jp		z,retorna		;chegou ao fim da tabela
				cp		a,CODBPMY
				jp		nz,g5
				%rept		5
				inc		hl		;desloco HL para prox.placa
				%endrept
				ld		a,(hl)
				cp		a,CODBPMP
				jp		nz,g5		;se a prox.placa for BPMP
				%rept		5
				inc		hl
				%endrept		;entao desloco mais 5 posicoes em HL
g5:				inc		ix
				ld		a,(ix)		; leio flag
				cp		a,NAO_CICLAVEL
				jp		nz,continua
				dec		ix
				ld		a,OPR_PROXIMA
				ld		(tipo_opr),a		;carrego com OPR_PROXIMA
				call		executa
				jp		inicio
continua			ld		a,(ix)		; leio flag
				push		af
				push		af
				and		%00011111		; desprezo set do num.pulso passos
				ld		(bc),a		; salvo flag
				ld		a,(hl)		;carrego tipo placa
				inc		bc
				ld		(bc),a		;salvo tipo placa
				pop		af
				and		%00100000
				srl		a
				srl		a
				srl		a
				srl		a
				srl		a
				inc		bc
				cp		0		; verifico o set de pulso entre passos:0->0 , 1->16
				jp		z,zero
				ld		a,%00010000		;carrego com dezesseis pulsos entre passos
zero				ld		(bc),a
				inc		hl
				inc		hl		;pulo tipo estendido
				inc		bc
				inc		bc		;pulo contador de pulsos
				ld		a,(hl)
				ld		(bc),a		;salvo end.base
				pop		af		; recupero flag
				and		%00011111
				cp		$0F
				jp		m, menor
				cp		16
				jp		z,bloc10
				cp		17
				jp		z,bloc20
				cp		18
				jp		z,bloc30
				cp		19
				jp		z,bloc40
				cp		20
				jp		z,bloc50
				cp		21
				jp		z,bloc60
				cp		22
				jp		z,bloc70
				cp		23
				jp		z,bloc80
				cp		24
				jp		z,bloc90
				cp		25
				jp		z,blocA0

bloc10:				ld		a,$10
				jp		g2
bloc20:				ld		a,$20
				jp		g2
bloc30:				ld		a,$30
				jp		g2
bloc40:				ld		a,$40
				jp		g2
bloc50:				ld		a,$50
				jp		g2
bloc60:				ld		a,$60
				jp		g2
bloc70:				ld		a,$70
				jp		g2
bloc80:				ld		a,$80
				jp		g2
bloc90:				ld		a,$90
				jp		g2
blocA0:				ld		a,$A0

g2:				inc		bc
				ld		(bc),a		; salvo end+ pont.dados
				inc		bc
				xor		a
				ld		(bc),a		; salvo 0 em end- pont.dados
				inc		bc		; posiciono em flag
				jp		incrementa
menor				inc		bc		; ponteiro +sig end.dados
				push		af
				xor		a
				ld		(bc),a		; salvo 0 em end+ pont.dados
				pop		af
				inc		bc
				ld		(bc),a		; salvo end- pont.dados
				inc		bc		; posiciono em flag
incrementa			inc		ix		;dado
				inc		ix		;dado
				inc		ix		;status
				inc		ix		;prioriade
				inc		hl
				inc		hl
				inc		hl
				jp		inicio

retorna				ret		; chegou ao final da tabela

	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;EXECUTA - executa a rotina correspondente `a placa dada
	;
	;Entrada - (HL) - tipo da placa
	;Saida - HL aponta para a proxima placa ou fim (tipo NAO_INSTALADA)
	;Destroi - A, DE
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

executa				ld		a, (hl)
				cp		NAO_INSTALADA		;placa existente?
				jp		nz, executa1		;sim
				%rept		5
				inc		hl		;proxima placa
				%endrept
				ret

executa1			inc		hl		;pula tipo
				inc		hl		;pula tipo estendido
				ld		a, (hl)
				ld		(base), a		;end. base
				inc		hl
				ld		e, (hl)		;end. desvio -sig
				inc		hl
				ld		d, (hl)		;end. desvio +sig
				inc		hl
				push		hl		;salva ponteiro para o mapa
				push		bc		;salva registradores
				ld		hl, exec_volta ;carrega o end. retorno
				push		hl		;		e o salva
				ex		de,hl		;coloca em HL o end. desvio
				jp		(hl)		;desvia para a posicao correspon-
				;dente na tabela e executa o
				;comando determinado em (tipo_opr)

exec_volta			pop		bc		;recupera registradores
				pop		hl		;recupera ponteiro para conf.
				ret

	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;BOOTA_PL - faz o boot para as placas encontradas
	;
	;Entrada - nao ha'
	;Destroi - A
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

boota_pl			ld		a, OPR_BOOT		;operacao que as rotinas executarao
				ld		(tipo_opr), a
				ld		hl, mapa_placa		;configuracao das placas
				ld		ix, buf_in		;inicio do bloco de entrada
				ld		iy, buf_out		;inicio do bloco de saida

boota_pl0			ld		a, (hl)
				cp		a, NAO_INSTALADA ;chegou ao fim?
				ret		z
				cp		a, CODSER2MBPS		;nao e' serial?
				jp		nz, boota_pl1
				%rept		5
				inc		hl
				%endrept
				jp		boota_pl0

boota_pl1			call		executa		;executa o boot para a placa
				jp		boota_pl0


	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;FAZ_CICLO - Realiza um ciclo de operacao (leitura
	;	ou escrita) para todas as placas existentes
	;
	;Entrada -
	;Altera - todos
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

faz_ciclo			ld		hl, mapa_placa ;mapa das placas existentes
				ld		ix, buf_in		;inicio do bloco de entrada
				ld		iy, buf_out		;inicio do bloco de saida

nova_placa			call		wdt
				ld		a, (hl)
				cp		NAO_INSTALADA		;chegou `a ultima placa?
				jp		z,fim_fazciclo ;sim, entao acabou ciclo atual
				cp		CODSER2MBPS		;e' a serial?
				jp		nz, placa_sim		;nao, entao executa

				%rept		5
				inc		hl		;sim, entao pula para a proxima
				%endrept

				jp		nova_placa

placa_sim			ld		a, (operacao)
				ld		(tipo_opr), a
				cp		OPR_ESCRITA		;e' operacao de escrita?
				jp		z, e_prior		;se flag <> 0, deve fazer ajuste

				ld		a, (ix)		;ainda nao; ve prioridade da placa
				cp		0		;prioridade e' 0?
				jp		nz, pri_val		;nao
				ld		a, 1		;sim, entao e' invalida
				ld		(ix), a		;e dai' passa a ser 1
				jp		e_prior

pri_val				cp		1		;prioridade 1 e' sempre atendida
				jp		z, e_prior
				ld		c, a		;C:= prioridade da placa
				ld		a, (ciclo)		;A:= ciclo atual

verif_prior			sub		c		;senao, verifica a prioridade
				jp		z, e_prior		;se A mod C = 0, tem prioridade atual
				jp		m, nao_prior		;se A mod C <> 0, proxima placa
				jp		verif_prior

e_prior				ld		a, (operacao)		;placa tem prioridade, entao
				ld		(tipo_opr), a		;sera' feita a operacao
				call		executa
				jp		nova_placa		;e ira' para a proxima placa

nao_prior			ld		a, OPR_PROXIMA ;passara' os apontadores
				ld		(tipo_opr), a		;para as posicoes da
				call		executa		;proxima placa
				jp		nova_placa

fim_fazciclo			ld		a,(sincrono)
				cp		0
				ret		z		;se=0 entao nao hab.int.
				ei

				ret

	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;RECEBE_BUFFER - Copia do bloco dado da DPM para
	;		um buffer
	;Entrada - A = numero do bloco / BC = inicio do buffer
	;Altera	- todos
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

recebe_buffer			out		($03), a		;seleciona o bloco
				in		a, ($00)
				ld		(comando), a		;comando
				in		a, ($00)
				ld		(tamanho), a		;numero de bytes de dados
				ld		h, a		;contador dos bytes de dados
				inc		h

recbuf1				dec		h		;chegou ao ultimo dado?
				ret		z		;entao volta
				in		a, ($00)		;nao: pega da DPM
				ld		(bc), a		;escreve o byte no buffer
				inc		bc		;prox. posicao do buffer
				jp		recbuf1


	;;;;;;;;;;;;;org $222F


	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;ENVIA_BUFFER - Copia de um buffer para um
	;		bloco dado da DPM
	;
	;Entrada - A = numero do bloco / BC = inicio do buffer
	;		H = numero de bytes de dados
	;Altera	- todos
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

envia_buffer:			out		($03), a		;seleciona o bloco
				push		af
				in		a, ($00)		;pula comando
				ld		a, h
				out		($00), a		;numero de bytes de dados
				inc		h

envbuf1:			dec		h		;chegou ao ultimo byte?
				jp		z, envbuf2
				ld		a, (bc)		;pega o byte do buffer
				out		(00), a		;escreve na DPM
				inc		bc		;prox. posicao do buffer
				jp		envbuf1

envbuf2:			pop		af
				out		($03), a		;seleciona o bloco novamente
				ld		a, (codigo_atual)
				out		($00), a		;envia o comando
				ret

	;*********************************************
	; Tratamento da interrupcao mascaravel
	;*********************************************

atualiza_cont:
				; atualiza contador de pulsos durante atendimento
				ld		a, (pulsos_durante_trata)
				inc		a
				ld		(pulsos_durante_trata), a

				ex		af, af'		; recupera registrador AF
				ei
				reti

	;*************************************************************************

trata_int:			ld		a, (int_em_trata)
				cp		1
				jp		z, atualiza_cont		;ja' esta' tratando uma int?
				; entao so' atualiza cont. pulsos
				ex		af, af'		;recupera AF que a verificacao
				; da interrupcao guardou

				push		af		;guarda todos
				push		bc		;os registradores que
				push		de
				push		hl		;serao usados

				xor		a
				ld		(pulsos_durante_trata), a
				ld		a, 1
				ld		(int_em_trata), a		; sinaliza em tratamento

				ld		(primeira), a		; (primeira placa sera' tratada):= 1
				ei		; e volta a permitir novas
				; interrupcoes
				ld		hl, mapa_rampa

	;*************************************************************************
	;*************************************************************************

prox_rampa:			ld		a, (hl)
				cp		NAO_INSTALADA		;acabaram as placas com rampa?
				jp		z,fim_rampa

				inc		hl		; salta tipo
				inc		hl		; salta sequencia

				ld		a, (hl)		; pulsos entre passos
				inc		hl
				inc		(hl)		; incr. cont de pulsos
				ld		c, (hl)		; contador de pulsos
				cp		c		; compara e ve se e' hora de fazer para
				; essa placa
				jp		z, faz_rampa
				jp		c, faz_rampa		; caso "escape" e ultrapasse os valores
				; admitidos (1 ou 4)

				; nao, nao e' hora desta placa

				ld		de, TAM_MAPARAMPA - 3
				add		hl, de		; vai para a proxima no mapa
				xor		a
				ld		(primeira), a
				jp		prox_rampa


	;*************************************************************************
	;*************************************************************************

faz_rampa:			call		wdt
				ld		(hl), 0		;zera cont. pulsos
				inc		hl		;
				ld		c, (hl)		;end. base
				inc		hl

				ld		a,(hl)		;conserva o substep que sera' usado
				inc		(hl)		;mas ja' incrementa o proximo substep

				cp		0		;compara o substep atual
				jp		z,faz_step0
				cp		1		; 1/4
				jp		z,faz_step1
				cp		2		; 1/2
				jp		z,faz_step2

				;		senao...

				jp		faz_step3		; 3/4


	;----------- faz para dado i
	; primeiro ponto, calcula substeps

faz_step0:			ld		a, (primeira)
				cp		1
				jp		nz, rampa_naoprimeira

				xor		a
				ld		(primeira), a
				ld		de, (cont_passos)
				inc		de
				ld		(cont_passos), de

rampa_naoprimeira:
				ld		de, 5		;pula numero de passos (2 bytes) e
				add		hl, de		;ponteiro inicio (2 bytes)

				;HL - aponta para a posicao do mapa_rampa que contem o endereco
				;		do passo atual
				;-----

				push		bc		;#1 - reserva end. base
				push		hl		;#2 - reserva pos. HL
				exx

	;------ alternativos

				pop		hl		;@2 - recupera em HL' := HL
				ld		b, (hl)
				inc		hl
				ld		c, (hl)
				inc		bc		; BC' aponta para dado i+1
				inc		bc
				ld		(hl), c
				dec		hl
				ld		(hl), b		; (HL') aponta para dado i+1

				ld		a, (bc)
				ld		d, a
				inc		bc
				ld		a, (bc)
				ld		e, a		; DE' contem dado i+1

				dec		bc
				dec		bc
				ld		a, (bc)		; -sig. dado i
				ld		l, a
				dec		bc		; +sig. dado i
				ld		a, (bc)
				ld		h, a		; HL' contem dado i
				push		hl		; #2 - reserva dado i

	;********	calcula 1/2

				add		hl, de
				rr		h
				rr		l		; HL':= meio
				push		hl		; #3 - reserva meio

				exx		;------------ volta aos oficiais

				inc		hl
				inc		hl		; HL aponta para caixa do dado i
				pop		bc		; @3 - recupera BC:= meio
				pop		de		; @2 - recupera DE:= dado i

				ld		(hl), d		; *** guarda na caixa o dado i
				inc		hl
				ld		(hl), e
				inc		hl
				ld		(hl), b		; *** guarda na caixa o meio
				inc		hl
				ld		(hl), c
				inc		hl

	;********	calcula 1/4

				ld		a, e		; soma -sig. dado i e meio (E+C)
				add		c
				ld		c, a
				ld		a, d
				adc		a, b		; soma +sig. dado i e meio (D+B) com carry
				rr		a		; roda +sig.
				ld		(hl), a		; e guarda na caixa do 1/4
				inc		hl
				rr		c		; roda o menos significativo
				ld		(hl), c		; *** e guarda na caixa do 1/4
				inc		hl
				push		hl		; #2 - guarda a pos. caixa 3/4

	;------ alternativos

				exx
				pop		bc		; @2 - recupera em BC'
				; apontador para a caixa do 3/4

	;*********	calcula 3/4
				add		hl, de		; HL':= HL' + DE' : meio+dado i+1
				rr		h
				rr		l		; HL':= valor		3/4
				ld		a, h
				ld		(bc), a
				inc		bc
				ld		a, l
				ld		(bc), a		; *** guarda na caixa do 3/4

				exx		;------------ volta aos oficiais
				;HL continua com a pos. caixa do 3/4

				pop		bc		; @1 - recupera end.base em C

				inc		hl
				inc		hl		; (salta 3/4)

; tudo calculado, ja'		pode fazer
				jp		atua_rampa

	;----------- faz para 1/4
faz_step1:			ld		de, 11
				add		hl, de		;atuara' com o valor de 1/4
				ld		d, (hl)		;pega o valor de ajuste +sig
				inc		hl
				ld		e, (hl)		;pega o valor de ajuste -sig
				inc		hl

				inc		hl		;(salta 3/4)
				inc		hl
				jp		atua_rampa

	;----------- faz para 1/2
faz_step2:
				ld		de, 9		;faz com o valor de 1/2
				add		hl, de
				ld		d, (hl)		;pega o valor de ajuste +sig
				inc		hl
				ld		e, (hl)		;pega o valor de ajuste -sig
				inc		hl

				inc		hl		;(salta 1/4)
				inc		hl
				inc		hl		;(salta 3/4)
				inc		hl

				jp		atua_rampa

	;----------- faz para 3/4
faz_step3:
				ld		(hl),0		;seta para valor de passo de tabela
				;mas agora faz com o valor de 3/4
				ld		de, 13
				add		hl, de
				ld		d, (hl)		;pega o valor de ajuste +sig
				inc		hl
				ld		e, (hl)		;pega o valor de ajuste -sig
				inc		hl

	;-----------
atua_rampa:			; posicionado no indicador de
				ld		a, (hl)		; tipo de rotina (12 ou 16 bits)
				cp		DEZESSEIS_BITS
				inc		hl		; ja' posiciona na proxima do mapa
				jp		z, placarampa16

	;-=-----------------------------------------------
	; - faz ajuste (em 12 bits) do dado, com +sig no D e -sig no E

placarampa12:
				ld		a, d
				sla		a		;dado + sig
				sla		a
				sla		a
				sla		a
				ld		d, a
				ld		a, e		;dado - sig
				srl		a
				srl		a
				srl		a
				srl		a
				or		d
				out		(c),a		;dado + sig (c=00)
				inc		c		;01
				ld		a,e
				sla		a
				sla		a
				sla		a
				sla		a
				out		(c),a		;dado - sig
				inc		c		;02
				out		(c),a		;converto
				jp		prox_rampa

	;**************
	;-=-----------------------------------------------
	; - faz ajuste (em 16 bits) do dado, com +sig no D e -sig no E

placarampa16:			ld		a, d
				out		(c),a		;mais sig (end. base)
				inc		c		;01
				ld		a, e
				out		(c),a		;dado - sig
				inc		c
				out		(c), a		;disparo
				jp		prox_rampa

	;-=-----------------------------------------------

fim_rampa:			;------ acrescentado em 08/12 para evitar necessidade
				;de pulso adicional

				di
				xor		a
				ld		(int_em_trata), a

				ld		a, (pulsos_durante_trata)
				cp		0
				jp		Z, verifica_fimrampa		; se nao houve algum
				; pulso "cego", ve se deve continuar
				; a rampa no futuro

				; sim, houve pulsos durante o tratamento
				ld		hl, mapa_rampa		; sim, entao contabiliza-o
				ld		bc, TAM_MAPARAMPA - 3		; adicionando para todas

ajuste_pulsoscegos:
				inc		hl
				inc		hl
				inc		hl		;posiciona em cont. pulso
				ld		a, (pulsos_durante_trata)
				add		a, (hl)		;cont.pulso+= pulsos_cegos
				ld		(hl), a		;ocorridos durante tratamento int.
				add		hl, bc
				ld		a, (hl)
				cp		NAO_INSTALADA
				jp		nz, ajuste_pulsoscegos

				xor		a
				ld		(pulsos_durante_trata), a


verifica_fimrampa:
				ld		hl, (cont_passos)
				ld		a, (mapa_rampa+7)		; cont. passo -sig. 1a. placa
				ld		c, a
				cp		l
				jp		nz, rampa_continuara
				ld		a, (mapa_rampa+6)		; cont. passo +sig. 1a. placa
				ld		b, a
				cp		h
				jp		nz, rampa_continuara

				ld		a, DISPLAY_MENOS
				ld		(display), a
				call		wdt
				ld		a, CMD_FIMRAMPA
				ld		(codigo_atual), a
				ld		a, BLOCO_SAIDA
				ld		c, CMD_FIMRAMPA		;envia comando rampa completada
				call		Sinaliza
				jp		rampa_terminou

	;-=-----------------------------------------------

rampa_continuara:
				pop		hl
				pop		de
				pop		bc
				pop		af
				ei		;deixa pronta para uma interrupcao e
				reti		;retorna da chamada

rampa_terminou:
				ld		a, (rampa_ciclica)
				cp		1
				jp		nz, nao_ciclica_mesmo ; nao e'		ciclico, termina mesmo

				; sim, e' ciclica!

				ld		hl, 0
				ld		(cont_passos), hl		;zera contador de passos

				ld		a, (mapa_rampa+6)		;numero de passos definido
				ld		(total_passos+1), a ;		para a rampa da
				ld		a, (mapa_rampa+7)		;		primeira placa
				ld		(total_passos), a

				ld		hl, mapa_rampa
labRampaCic1:			ld		a, (hl)
				cp		NAO_INSTALADA		;chegou ao fim da lista?
				jp		z, labRampaCic2

				inc		hl		;reinicializa ponteiros e
				inc		hl		;contadores no mapa de rampa
				inc		hl
				ld		(hl), 4		;zera contador de pulsos (=4) 15/02/01
				inc		hl
				inc		hl		; salta end. base
				ld		(hl), 0		;zera subpasso
				inc		hl		;
				inc		hl		;salta num. passos
				inc		hl		;
				ld		b, (hl)		;ponteiro inicial (+sig.)
				inc		hl
				ld		c, (hl)		;ponteiro inicial (-sig.)
				inc		hl
				ld		(hl), b		;move o ponteiro de dados
				inc		hl		;para o endereco inicial
				ld		(hl), c		;de rampa da placa
				ld		bc, TAM_MAPARAMPA - 11
				add		hl, bc
				jp		labRampaCic1		;proxima placa

labRampaCic2:			ld		a, BLOCO_ENTRADA
				ld		c, CMD_NORMAL
				call		Sinaliza		;limpo o byte recebido com
				;o comando

				ld		a, DISPLAY_SUBIDA ;a:= "rampa"
				ld		(display), a		;mostra no display
				call		wdt
				ld		a, INT_RAMPA
				ld		(interrupt),a

				xor		a
				ld		(int_em_trata), a ; sinaliza que ainda nao esta'
				; em tratamento
				pop		hl
				pop		de
				pop		bc
				pop		af		; volta habilitando interrupcao
				ei		;habilita interrupcao
				reti

nao_ciclica_mesmo:

				pop		hl
				pop		de
				pop		bc
				pop		af		; volta sem habilitar interrupcao

				reti


	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;;;;;; TRATAMENTO DE INTERRUPCAO PARA CICLAGEM ;;;;;;
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

trata_int2			push		af
				push		bc
				push		de
				push		hl
				ld		bc,(total_passos)
				ld		hl,(cont_passos)
				ld		a,c
				cp		l
				jp		nz,faz_trata2
				ld		a,b
				cp		h
				jp		nz,faz_trata2
				xor		a
				ld		(interrupt),a		;destroi tipo de trat de interrup(ciclagem)
				ld		a,CMD_CICLCOMP
				ld		(codigo_atual),a		;a:=termino de ciclagem
				ld		a, DISPLAY_IGUAL
				ld		(display),a

envia_termino			call		wdt
				push		hl
				push		bc
				ld		a,BLOCO_SAIDA
				call		envia_buffer		;fico enviando CMD_CICLCOMP ate receber
				pop bc
				pop hl
				ld		a,BLOCO_ENTRADA		;o comando CMD_MSGRECEB
				out ($03),a
				in		a,($00)
				cp		CMD_MSGRECEB
				jp		nz,envia_termino
				jp		cicla_terminou
faz_trata2			ld		a,(num_pulsos)
				inc a
				ld		(num_pulsos),a		;atualizo num_pulsos
				cp		16		;ja chegaram num_pulsos necessarios para 12b?
				jp		nz,g1
				ld		de,(pont_d12b)
				inc		de
				inc		de
				ld		(pont_d12b),de		;atualizo pont_d12b para passo atual
				xor		a
				ld		(num_pulsos),a		;zero num_pulsos
g1:				ld		hl,mapa_cicla
				inc hl				;posiciono em tipo placa
				push		hl
				ld		hl,(cont_passos)
				inc hl
				ld		(cont_passos),hl		;incrementa contador de passos
				pop hl
				call		wdt
prox_cicla			ld		a,(hl)
				cp		NAO_INSTALADA
				jp		z,fim_cicla
				ld		d,a		;salvo tipo placa
				inc hl
				ld		a,(hl)
				ld		e,a		;salvo pulso entre passos
				inc hl
				ld		a,(hl)		;carrego contador de pulsos
				inc a				;incremento contador de pulsos
				cp		e		;comparo com pulso entre passos
				jp		z,atua_cicla		;chegaram num.pulsos necessarios?
				jp		p,atua_cicla
				ld		(hl),a		;atualizo contador de pulsos
				ld		bc,5
				add hl,bc			; aponta para proxima placa
				jp		prox_cicla
atua_cicla			xor		a
				ld		(hl),a		;zero contador de pulsos
				inc hl
				ld		c,(hl)		;pego end.base da placa
				inc hl				;posiciono em end+ pont.inic.dados
				ld		a,(hl)
				cp		0
				jp		z,p12bits
				out ($ff),a			;seleciono bloco para locon16b
				ld		(bloco),a
				inc hl
				inc hl
				inc hl				;proxima placa
				jp		cicla1
p12bits				inc hl		;posiciono em end- pont.inic.dados
				ld		a,(hl)
				out ($ff),a			;seleciono bloco para locon12b
				inc hl
				inc hl				;proxima placa

cicla1				ld		a,DISPLAY_C
				ld		(display),a
				ld		a,d		;carrego tipo placa
				cp		CODLCN12BMP		;e executa correspondente
				jp		z,placa_12a
				cp		CODLCN12BBP
				jp		z,placa_12a
				cp		CODLCN16BMP
				jp		z,placa_16a
				cp		CODLCN16B32MM
				jp		z,placa_16a
				cp		CODLCN16BBP
				jp		z,placa_16a
placa_12a			ld		de,(pont_d12b)
				inc de
				ld		a,(de)
				ld		b,a		;pego dado -sig
				dec de
				ld		a,(de)		;pego dado +sig
				sla a
				sla a
				sla a
				sla a				;dado +sig
				ld		d,a
				ld		a,b		;dado -sig
				ld		e,a
				srl a
				srl a
				srl a
				srl a
				or		d
				out		(c),a		;dado +sig(c=00)
				inc c				;01
				ld		a,e
				sla a
				sla a
				sla a
				sla a
				out (c),a			;dado -sig
				inc c				;02
				out (c),a			;converto
				jp		prox_cicla
placa_16a:
				ld		de,(pont_d16b)
				inc de
				ld		a,(de)
				ld		b,a		;pego dado -sig
				dec de
				ld		a,(de)		;pego dado +sig
				out (c),a			;dado +sig end.base
				inc c				;01
				ld		a,b
				out (c),a		;dado-sig		end.base+1
				dec		c		;base
				push		de
				in		a, (c)		;(end. base) le configuracao de
				ld		d, a		;offset
				inc		c		;(base + 1)
				in		a, (c)		;le configuracao de ganho
				inc		c		;(base + 2)
				out		(c), a		;ajusta ganho
				inc		c
				inc		c
				inc		c		;(base + 5)
				out		(c), d		;ajusta offset
				pop		de
				inc de
				inc de
				ld		a,e
				dec de
				dec de
				cp		$00
				jp		nz,prox_cicla
				inc de
				inc de
				ld		a,d
				dec de
				dec de
				cp		$a0		;verifico se chegou na ultima posicao do bloco
				jp		nz,prox_cicla
				ld		a,(bloco)
				inc a
				ld		(bloco),a
				dec		hl
				dec hl
				dec hl				;posiciono novamente em end+ ponteiro inic. dados
				ld		(hl),a
				inc hl
				inc hl
				inc hl				;posiciono hl para prox.placa
				jp		prox_cicla
fim_cicla			ld		de,(pont_d16b)
				inc de
				inc de
				ld		a,e
				cp		$00
				jp		nz,atual_pont
				ld		a,d
				cp		$a0
				jp		nz,atual_pont
				ld		e,$00
				ld		d,$80		;posiciono pont_dados para inicio do prox.bloco
atual_pont			ld		(pont_d16b),de		;atualizo ponteiro para prox.dado
				pop hl
				pop de
				pop bc
				pop af

				ei
				reti

cicla_terminou			pop hl
				pop de
				pop bc
				pop af
				in		a,($ff)		;seleciono ram_int
				reti


	;### Tratamento de interrupcao para CMD_AJUSTE2 #######;

trata_int3			push		af
				push		bc
				push		de
				push		hl
				call		wdt
				ld		hl, mapa_placa		;mapa das placas existentes
novaplaca			ld		a, (hl)
				cp		NAO_INSTALADA		;chegou `a ultima placa?
				jp		z,fim_disp		;sim entao finalizar trata_int3
				cp		CODLCN12BMP
				jp		z,ajusta
				cp		CODLCN12BBP
				jp		z,ajusta
				cp		CODLCN16BMP
				jp		z,ajusta
				cp		CODLCN16B32MM
				jp		z,ajusta
				cp		CODLCN16BBP
				jp		z,ajusta
				%rept		5
				inc		hl		;sim, entao pula para a proxima
				%endrept
				jp		novaplaca

ajusta				inc		hl		;tipo estendido
				inc		hl		;end.base
				ld		a,(hl)
				ld		c,a		;c recebe end. base
				inc		c		;base + 1
				inc		c		;base + 2
				out		(c),a		;disparo DAC
				inc		hl		;end.trat.-sig
				inc		hl		;end.trat.+sig
				inc		hl		;tipo prox.placa
				jp		novaplaca


fim_disp			xor		a
				ld		(sincrono),a		;reseto sincrono
				ld		(interrupt),a		;reseto interrupt
				pop		hl
				pop		de
				pop		bc
				pop		af
				reti

	;##############################################
	;### Tratamento de interrupcao BPMs - booster;
	;##############################################

trata_bpm			push		af

	;---- Verifica se ja' passaram os pulsos necessarios para iniciar integracao

				ld		a, (conta_pulsosBPM)		;ja' passaram os pulsos para
				dec		a		;uma nova integracao?
				jp		z, vesepodefazer_bpm		;sim
				ld		(conta_pulsosBPM), a		;nao, entao volta
				call		wdt

				pop		af
				ei
				reti

vesepodefazer_bpm:

				ld		a, (conta_passosBPM)		;nao, entao volta
				dec		a
				jp		z, podefazer_bpm
				ld		(conta_passosBPM), a
				call		wdt

				pop		af
				ei
				reti

	;---- sim, mas ha' um limite de 20 coletas de integracao. Ainda vai fazer?

podefazer_bpm			ld		a, (pulsos_entreBPM)		;contador voltara' a esperar
				ld		(conta_pulsosBPM), a		; um certo numero de passos ate' a
				; a proxima integracao
				ld		a, 4		;		e que volte a zero o
				ld		(conta_passosBPM), a		;contador do pulso para passo (1 a 4)

				ld		a, (integracoesBPM)		;verifica se ja' foram feitas
				cp		20		;as 20 integracoes?
				jp		nz, faz_bpm

	;---- nao! ja' foram feitas as 20 coletas
				di
				xor		a		;A:= 0
				call		wdt
				pop		af		;		recupera pilha e volta
				reti

	;---- sim, deve fazer a coleta de integracao!
faz_bpm				inc		a
				ld		(integracoesBPM), a		;atualiza contador de integracoes
				push		bc
				push		de
				push		hl
				call		bpm64_0		;faz a leitura

				ld		bc, buf_in		;parametros: ponteiro buffer de entrada
				ld		a, (tam_in)		;
				ld		h, a		;		tamanho
				ld		a, BLOCO_SAIDA		;		bloco 0
				call		envia_buffer		;transfere o buffer para DPM

				pop		hl
				pop		de
				pop		bc
				pop		af
				ei
				reti


	;##########################################################################
	;### Tratamento de interrupcao para verificar posteriormente DCCT - booster
	;##########################################################################

trata_waitdcct			push		af		;interrupcao gerada pelo
				push		bc		;		relogio geral BOOster
				push		de
				push		hl
				call		wdt

				ld		a, (endbase_fotdcct)
				add		3
				ld		c, a		;e obtem end. base contadora dcct

				;programa atraso no delay

				ld		a, 7
				out		(c), a		;end. base+3 := 7
				inc		c
				inc		c
				out		(c), a		;end. base+5 := 0
				dec		c
				dec		c
				dec		c
				ld		a, (delayDCCT+2)
				out		(c), a		;end. base+2 := ++sig delay dcct
				dec		c
				ld		a, (delayDCCT+1)
				out		(c), a		;end. base+1 := +-sig delay dcct
				dec		c
				ld		a, (delayDCCT)
				out		(c), a		;end. base		:= --sig delay dcct
				inc		c
				inc		c
				inc		c
				xor		a
				out		(c), a		;end. base+3 := 0
				inc		c
				inc		c
				out		(c), a		;end. base+5 := 0		dispara


	;----------------- quando acontecer a interrupcao...

				ld		a, INT_LEITDCCT		;vai aguardar estourar o prazo
				ld		(interrupt), a		;da contadora para ler o DCCT

				pop		hl
				pop		de
				pop		bc
				pop		af
				ei
				reti

	;####################################################################
	;### Tratamento de interrupcao p/ leitura imediata da DCCT - booster
	;####################################################################

trata_leitdcct			push		af		;interrupcao gerada pela
				push		bc		;		contadora de fotons
				push		de
				push		hl
				push		ix
				push		iy
				call		wdt

				ld		a, (endbase_fotdcct) ;obtem end. base CONTDCCT
				add		3
				ld		c, a
				ld		a, 7
				out		(c), a		; end+3:= 7
				inc		c
				inc		c
				out		(c), a		; end+5 -> zera e para contadora
				dec		c
				dec		c
				dec		c
				ld		a, (delayDCCT)
				out		(c), a		;so' para refrescar
				dec		c
				ld		a, (delayDCCT+1)
				out		(c), a		;so' para refrescar
				dec		c
				ld		a, (delayDCCT+2)
				out		(c), a		;so' para refrescar

				;----- faz leitura dcct
				ld		a, (endbase_locondcct) ;obtem end. base LOCON DCCT
				ld		c, a
				ld		ix, buf_tmp		;buffer tempor. com leitura
				call		lcn12bmp0
				ld		ix, buf_tmp

				ld		hl, (corrlimite)
				ld		a, (ix+2)		;AD+sig
				cp		h
				jp		c, abaixo_dcct		; leitura dcct < limite?

				jp		nz, acima_dcct		; leitura dcct >> limite

				ld		a, (ix+3)		;se limite+sig = AD+sig
				cp		l		;		entao testa o -sig
				jp		c, abaixo_dcct

;=======			CORRENTE SUPEROU O LIMIAR!

				;----- desabilita HINJ
acima_dcct			ld		a, (endbase_relBOO) ;end. base do relogio Booster
				add		a, 4		;end.base + 4: HINJ
				ld		c, a
				xor		a
				out		(c), a		;escreve 0 em HINJ, desabilitando

				;----- coloca 1a. velocidade da tabela
				ld		(faixaveloc), a		;(faixaveloc):= 0

				ld		ix, (ix_relogboo)
				ld		(ix+2), STBOO_RAMPAFAIXAS-1

				ld		ix, tabvelocBOO		;aponta a velocidade
				ld		(indtabveloc), ix		;e guarda

				call		muda_velocBOO
				;---------------------------------
				xor		a
				ld		(em_deteccao), a		;desabilita deteccao
				ld		a, INT_RAMPABOO		;e passara' a receber aviso
				ld		(interrupt), a		;de mudanca de velocidades

				jp		fim_dcct

				;======= NAO, corrente nao atingiu o limiar

abaixo_dcct			ld		a, INT_WAITDCCT		;vai aguardar o proximo pulso
				ld		(interrupt), a		;		do relogio
				ld		a, (endbase_fotdcct) ;obtem end. base CONTDCCT
				add		3
				ld		c, a
				xor		a
				out		(c), a		; end+3:= 0
				inc		c
				inc		c
				out		(c), a		; end+5 -> dispara cont. pulsos

				;------------------------------------------

fim_dcct			pop		iy
				pop		ix
				pop		hl
				pop		de
				pop		bc
				pop		af
				ei
				reti

	;##########################################################################
	;### Tratamento de interrupcao para leitura da serial 232
	;##########################################################################

trata_232			push		af
				push		bc
				push		de
				push		hl
				push		ix

				ld		a, (base)
				ld		c, a
				call		avanca		;posiciona C em base+4
				xor		a
				ld		(time_out232), a		;inicializa var: time-out=0
				call dr				;testa data ready
				ld		a, (time_out232)
				cp		1
				jp		z, ignora232		;deu time-out? descarta

				in		a,(c)		;le o caracter disponivel

				ld		ix, (respIX232)
				ld		(ix),a		;guarda leitura
				inc		ix		;incrementa ponteiro da
				ld		(respIX232), ix		;		posicao atual da resposta

ignora232:			ld		a, (qtcarac232)
				inc		a
				ld		(qtcarac232), a		;atualiza contador de caract. lidos
				cp		TOTCARAC232
				jp		nz, continua232		;chegaram todos os esperados?

acabou232:			xor		a		; ------- sim, acabaram os 17 carac.
				ld		de, (tentou232)
				ld		de, 0
				ld		(tentou232), de
				ld		(interrupt), a		;termina com a interrupcao
				ld		de, (pontoix232)		;copia da var. de resposta para o
				ld		hl, leit232		;		bloco de saida que ira' para a DPM
				ld		bc, TOTCARAC232
				ldir

				pop		ix
				pop		hl
				pop		de
				pop		bc
				pop		af
				reti		;- volta sem habilitar

continua232:			pop		ix		;- nao acabou, volta habilitando
				pop		hl
				pop		de
				pop		bc
				pop		af
				ei
				reti

	;----------------------------------------------------------

avanca				inc		c
				inc		c
				inc		c
				inc		c		;base +4 =Modem Control Register PC16550D
				ret

recua				dec		c
				dec		c
				dec		c
				dec		c		; base
				ret

temt:
				%rept		5
				inc		c		;vou verificar o Transmitter Empty
				%endrept		;base +5
temt1				in		a,(c)
				and		%01000000
				jp		z,temt1
				%rept		5
				dec		c
				%endrept		;base
				ret

	; data ready com time out

dr:				call		wdt
				ld		de,0
				inc		c		;base +5

dr1:				inc		de
				in		a,(c)
				and		a,1		;testo DR (bit 0)
				jp		nz,dr2		;sim, ha'
				ld		a,d
				cp		95		;nao, nao ha' caracter
				jp		nz,dr1		;esgotou sem chegar caracter?
				ld		a, 1
				ld		(time_out232),a		;coloca valor <> 0 para marcar time-out

dr2:				dec		c		;retorna
				jp		recua		; volta com C no end. base e retorna

	;##########################################################################
	;### Tratamento de interrupcao para verificar passo de RAMPA - booster
	;##########################################################################

				;significa que chegou em um patamar na tabela de velocidade

trata_rampaboo			push		af		;interrupcao gerada pelo
				push		bc		;		relogio de rampa
				push		de
				push		hl
				push		iy
				push		ix
				call		wdt

				;---- desliga relogioRSINC
				ld		a, (endbase_relRSINC)
				ld		c, a		;recupera end. base relRSinc
				inc		c
				inc		c		;end. base+2
				ld		a, DESL_RELOG
				out		(c), a		;escreve "desliga relogio"

				;---- faz a leitura do passo
				ld		a, (endbase_fotsinc)
				ld		c, a
				in		a,(c)		;end. base: dado --sig.
				ld		e, a		;
				inc		c		;end. base+1 da contadora
				in		a,(c)		;dado +-sig.
				ld		d, a

				;----------------------------------- fez a leitura do passo
				;		verifica se e' um passo significativo (ultimo ou ejecao)

				ld		hl, (passoEJECrampa)
				ld		a, d		;+sig
				cp		h
				jp		c, ve_ultimo		;passo ejecao?

				ld		a, e		;-sig
				cp		l
				jp		nc, faz_ejecao

ve_ultimo			ld		hl, (ultpassorampaBOO)
				ld		a, d		;+sig
				cp		h
				jp		c, passo_veloc		;passo limite para mudar veloc.?

				ld		a, e		;-sig
				cp		l
				jp		nc, para_rampaBOO

				;----------------------------------
				;nao e' nem o ultimo, nem ejecao - programa contadora e continua

passo_veloc			call		muda_velocboo
				ld		a, d
				cp		255		;(A) retorna com 255 se nao ha' mais faixas
				jp		z, para_rampaboo
				jp		fim_trataboo

	;**********************************************************************
faz_ejecao			;faz Ejecao
				ld		a, (endbase_relBOO) ;end. base do relogio Booster
				add		a, 5		;end.base + 5: HEJE
				ld		c, a
				ld		a, 1
				out		(c), a		; pulsa HEJE, habilitando ejecao

				ld		ix, $FFFF
				ld		(passoEJECrampa),ix		; indica que nova ejecao nao vai mais ocorrer

				ld		ix, (ix_relogboo)
				ld		a, (ix+2)
				or		a, STBOO_EJETADO
				ld		(ix+2), a
				call		muda_velocboo
				jp		fim_trataboo

	;***********************************************************************

para_rampaBOO			ld		ix, (ix_relogboo)
				ld		a, (ix+2)
				or		a, STBOO_RAMPACOMPLETA
				ld		(ix+2), a

				ld		a, (endbase_fotsinc)
				add		a, 3
				ld		c, a
				call		desl_contboo

				;ja' havia desligado relogioRSINC, entao nao rehabilita
				;a interrupcao

	;=====================	Verifica se e' para fazer CICLICA

				ld		a, (rampa_ciclica)
				cp		1
				jp		nz, nao_ciclica		; nao e'		ciclico, termina mesmo

				;----------- e' ciclica, rearruma tudo!
				;---------------------------------------------------
				; comeca pelo atraso de espalhamento, se diferente de 0

				ld		a, (atraso_espalha)
				cp		0
				jp		nz, muda_espalha
				ld		a, (atraso_espalha+1)
				cp		0
				jp		z, nao_muda_espal
muda_espalha
				ld		a, (endbase_atrasboo)
				ld		c, a		;end.base Atraso
				ld		a, (atraso_espalha)
				out		(c), a
				inc		c		;end.base+1 Atraso
				ld		a, (atraso_espalha+1)
				out		(c), a

nao_muda_espal			; faz religar tudo para a proxima rampa ciclica

				; provoca atraso no sincronismo, antes de se preparar
				; para a proxima rampa

				ld		a, (atraso_ciclos)
atrasa_rampa:			cp		0
				jp		z, ja_atrasou
				call		wait10ms
				call		wdt
				dec		a
				jp		atrasa_rampa

ja_atrasou:			ld		a, (endbase_relboo)
				add		4
				ld		c, a		;end.base + 2
				ld		a, 255
				out		(c), a		;religa HINJ: end. base+4


				ld		a, DISPLAY_IGUAL
				ld		(display), a
				ld		(ix+2), STBOO_CORHABILITADA		; indica o estado

				ld		a, (endbase_fotsinc)
				add		a, 3
				ld		c, a
				call		zera_contboo

				ld		a, BLOCO_ENTRADA ;
				ld		c, CMD_NORMAL
				call		Sinaliza		;limpo o byte de comando de ajuste

				ld		a, 1
				ld		(em_deteccao), a		;sinaliza em deteccao de corrente
				ld		(relogboo_cego), a ;deixa a placa cega para novos ajustes
				ld		a, INT_WAITDCCT
				ld		(interrupt), a

				jp		fim_trataboo

	;=====================	terminou preparacao para ciclica

nao_ciclica:			xor		a
				ld		(interrupt), a
				ld		(relogboo_cego), a

				ld		a, (ix+2)
				or		a, STBOO_RAMPACOMPLETA
				ld		(ix+2), a

				pop		ix
				pop		iy
				pop		hl
				pop		de
				pop		bc
				pop		af
				reti

fim_trataboo			pop		ix
				pop		iy
				pop		hl
				pop		de
				pop		bc
				pop		af

				ei
				reti


	;----------------------------------------------------------------------------
	; Alterar velocidade de rampa do booster, para isso desligando e religando o relogio
	;----------------------------------------------------------------------------

muda_velocBOO			ld		a, (faixaveloc)
				cp		10
				jp		nz, faz_muda_vel
				ld		d, 255
				ret		z		;se ja' mudou 10 faixas, nada faz

faz_muda_vel			inc		a
				ld		d, a
				ld		(faixaveloc), a
				ld		ix, (ix_relogboo)
				inc		(ix+2)		;sinaliza prox. estado de faixa rampa

				ld		ix, (indtabveloc)

	;----- programa contadora de passos de rampa do booster (fotRSINC)

				ld		a, (endbase_fotsinc)
				add		3
				ld		c, a		;e obtem end. base contadora dcct

				;programa passos para a interrupcao

				ld		a, 1
				out		(c), a		;end. base+3 := 1
				inc		c
				inc		c
				out		(c), a		;end. base+5 := 0
				dec		c
				dec		c
				dec		c		;calcula-se F00000 - passos

				ld		a, (ix+1)
				cp		0
				jp		nz, veloc2boo01		;so' programa se for diferente de 0 passos
				ld		a, (ix)
				cp		0
				jp		nz, veloc2boo01
				ld		d, 255

veloc2boo01
				ld		a, $EF		;como passos sao dois bytes entao
				out		(c), a		;end. base+2 := ++sig passos (EF)
				dec		c
				dec		c
				ld		a, (ix+1)
				cpl
				add		a, 1
				out		(c), a		;end. base		:= --sig passos
				ld		a, (ix)
				cpl
				jp		nc, semcarry
				inc		a
semcarry			inc		c
				out		(c), a		;end. base+1 := +-sig passos
				inc		c
				inc		c
				xor		a
				out		(c), a		;end. base+3 := 0
				inc		c
				inc		c
				out		(c), a		;end. base+5 := 0		dispara

	;------------------------------------------------------------------

				ld		a, (endbase_relrsinc)		;end.base do relogio de rampa
				ld		c, a		;		do booster

				inc		c
				inc		c
				inc		c
				inc		c		;end.base+4
				ld		a, (ix+2)		;taxa repet +sig.
				out		(c), a
				inc		c		;end.base+5
				ld		a, (ix+3)		;taxa repet -sig.
				out		(c), a

				ld		a, (faixaveloc)
				ld		b, 0
				rlca
				ld		c, a
				ld		hl, tab_01+1
				add		hl, bc
				ld		a, (hl)
				cpl
				ld		(display_faixa), a
				ld		(display), a

				ld		bc, 4
				add		ix, bc		;aponta para a proxima velocidade
				ld		(indtabveloc), ix		;e guarda

				;religa relogRSINC
				ld		a, (endbase_relRsinc)
				ld		c, a		;recupera end. base relRSinc
				inc		c		;end. base+1
				ld		a, LIGA_RELOG
				out		(c), a		;escreve "liga relogio"
				ret

	;-----------------------------------------------------------------
zera_contboo			ld		a, 7
				jp		processa_contboo
desl_contboo			ld		a, 5
processa_contboo out		(c), a		;end. base+3 := 5 (desl.) ou 7 (zera)
				inc		c
				inc		c
				out		(c), a		;end. base+5 := 0		parou contboo
				dec		c
				dec		c
				dec		c
				out		(c), a		; so' para refrescar linhas de interr.
				dec		c
				out		(c), a
				dec		c
				out		(c), a
				ret

	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;PROC_PLACA - Detecta os tipos de placas existentes
	;		no bastidor e monta o mapa de configuracao
	;
	;Entrada
	;Saida
	;Destroi
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

proc_pl				ld		c, $03		;end. base: modulo 0, offset 0
				ld		a, $FF
				ld		(endbase_relboo), a		; anula indice da placa RELOGBOO no mapa

				xor		a
				ld		(tam_out), a		;zera contadores de bytes
				ld		(tam_in), a
				ld		(conta_placa), a		;zera contador de indice do mapa_placa

				ld		hl, mapa_placa		;HL aponta mapa de placas
				ld		b, $F0		;ultimo end. para offset 0 (mod. 15)

proc_ident			in		a,(c)		;le identificacao da placa
				dec		c
				dec		c
				dec		c		;C: end. base
				ld		de, tab_desvio		;tabela de enderecos de desvio
				srl		a
				srl		a
				cp		NUM_PLACAS		;ident. definida?
				jp		m, proc_val		;sim
				jp		proc_prox		;nao: tenta proxima

proc_val			ld		(hl), a		;salva tipo de placa
				inc		hl
				inc		hl		;pula tipo estendido
				ld		(hl), c		;salva end. base
				inc		hl

				push af

				cp		CODRELOGBOO		;tem placa de relogio de booster?
				jp		nz, proc_fotdcct
				ld		a, c
				ld		(endbase_relboo),a		;salva end. base do relogio de booster
				jp		proc_busca

proc_fotdcct
				cp		CODFOTODCCT
				jp		nz, proc_fotboo
				ld		a, c
				ld		(endbase_fotdcct), a ;salva end. base da contadora DCCT
				add		$10
				ld		(endbase_loconDcct), a ;salva end. base da locon DCCT
				jp		proc_busca

proc_fotboo
				cp		CODFOTOBOO
				jp		nz, proc_atboo
				ld		a, c
				ld		(endbase_fotsinc), a ;salva end. base da contadora passo rampa booster
				add		$10
				ld		(endbase_relrsinc), a ;salva end. base do relogio de rampa
				jp		proc_busca

proc_atboo			cp		CODATRASBOO
				jp		nz, proc_64
				ld		a, c
				ld		(endbase_atrasboo),a		;salva end. base de atraso do booster
				jp		proc_busca

proc_64				cp		CODBPM64		;tem placas de BPM64
				jp		nz, proc_busca
				ld		a, (bastidorBPM)		;e' bastidor BPM?
				cp		1
				jp		z, proc_busca
				ld		a, 1
				ld		(bastidorBPM), a		;marca bastidorBPM

				ld		a, c		;		end. base primeira placa
				add		a, 6		;guarda end. base + 6 (escrita dig.)
				ld		(endbaseBPM1), a
				add		a, $10		;guarda end. base + 6 (escrita dig.)
				ld		(endbaseBPM2), a
				sub		a, 6
				ld		c, a

				inc		hl
				inc		hl
				ld		a, CODBPM64
				ld		(hl), a
				inc		hl
				inc		hl
				ld		a, c
				ld		(hl), a
				inc		hl
				ld		(hl), LOW ser2mbps
				inc		hl
				ld		(hl), HIGH ser2mbps
				%rept		6
				dec		hl
				%endrept
				ld		a, (conta_placa);
				inc		a
				ld		(conta_placa), a

proc_busca			pop		af
proc_busca1			cp		0		;chegou `a posicao certa da tabela?
				jp		z, proc_achou		;sim
				inc		de
				inc		de
				dec		a		;nao, vai apontar para a proxima
				jp		proc_busca1

proc_achou
				%REPT		2
				ld		a, (de)		;coloca no mapa
				ld		(hl), a		;		end. desvio -sig e
				inc		hl		;		end. desvio +sig
				inc		de
				%ENDREPT

				ld		a, (conta_placa);
				inc		a
				ld		(conta_placa), a

proc_prox			ld		a, c
				cp		a, b		;chegou ao end. da ultima placa,
				;		para o offset em questao?
				jp		z, proc_plof8		;sim, passa para offset 8 ou termina
				add		a, $13		;nao: proxima placa
				ld		c, a
				jp		proc_ident

proc_plof8			cp		a, $F8		;chegou a` ult. placa, offset 8?
				ret		z		;sim, entao volta
				ld		c, $0B		;nao: end. primeira placa of. 8
				ld		b, $F8		;end. ultima placa offset 8
				jp		proc_ident


	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

				;		*********************************************
				; ** ***************************************** **
				; ** *		PROGRAMA PRINCIPAL		* **
				; ** ***************************************** **
				;		*********************************************

	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;;;;;	Reconhecimento das placas	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

prog_pri:			ld		a, DISPLAY_LIMPA
				ld		(display), a		;limpa o display

				xor		a
				ld		(em_deteccao), a ;inicializa

				ld		c, CMD_BOOT		;escreve "boot" no bloco 0
				ld		a, BLOCO_SAIDA
				call		Sinaliza

				call		inic_mapa		;inicializa o mapa de placas
				call		proc_pl		;procura placas no barramento

				ld		a, (bastidorBPM)
				cp		1
				jp		nz, pisca255
				ld		a, 4
				jp		setapisca
pisca255			ld		a,255
setapisca			ld		(num_ciclos),a		;255 ciclos de leitura para inversao do display

				call		wdt

	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;;;;;	B O O T	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

				xor		a
				ld		(sincrono),a		;reset no buffer de sincronismo de ajuste analogico
				call		boota_pl		;faz o boot para as placas
				call		wdt

				ld		hl, buf_rampa
				ld		(ptbuf_rampa), hl ;aponta para o inicio do buffer
				;		de rampa

				;;;; ajuste inicial (pos-boot) ;;;;;;;

				ld		hl, buf_out
				ld		a, OPR_ESCRITA		;operacao de escrita nas placas
				ld		(operacao), a
				call		faz_ciclo		;executa a operacao

				ld		a, (buf_in)		;mostra novamente a prioridade
				cpl
				ld		(display), a		;da primeira placa

	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;;;;;	Leitura inicial	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

				ld		a, OPR_LEITURA		;
				ld		(operacao), a		;faz o ciclo n. 1 de leitura
				ld		a, 1		;apenas para colocar o comando normal
				ld		(ciclo), a

				call		faz_ciclo		;

				ld		a, CMD_NORMAL		;prepara o envio do codigo de
				ld		(codigo_atual), a ;comando normal

				ld		bc, buf_in		;parametros: ponteiro buffer de entrada
				ld		a, (tam_in)		;
				ld		h, a		;		tamanho
				ld		a, BLOCO_SAIDA		;		bloco 0
				call		envia_buffer		;transfere o buffer para DPM

				ld		c, CMD_NORMAL		;comando de inicializacao concluida
				ld		a, BLOCO_SAIDA
				call		Sinaliza

				xor		a		;primeiro ciclo - 1
				ld		(ciclo), a

	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;;;;;;	Ciclos de Leitura	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

ini_ciclo:			ld		a, (ciclo)		;recupera o numero do ciclo
				call		wdt
				inc		a		;novo ciclo
				ld		b,a
				ld		a,(num_ciclos)
				cp		a,b
				jp		nz,plu_ciclo0

				;--- esta' mostrando as faixas? Entao tem que piscar
				ld		a, (display_faixa)
				cp		0
				jp		z, display_convencional
				cpl
				ld		e, a
				ld		a, (display)
				xor		e
				jp		display_mostrado

display_convencional
				ld		a, (display)		;indica 255 ciclos completos
				xor		$80		;		pela inversao do "." no display

display_mostrado
				ld		(display), a
				xor		a		;passa para o ciclo 0
				jp		plu_ciclo

plu_ciclo0: inc			b
				ld		a,b

plu_ciclo:			call		wdt
				ld		(ciclo), a		;salva numero do ciclo

	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;;;	LEITURA DAS PLACAS	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

				;--- caso bastidor BPM booster
				ld		a, (bastidorBPM)
				cp		0
				jp		z, nao_BPM64		;trata especial so' para os
				;		bastidores com novas BPM
				ld		a, (modorampaBPM)
				cp		0		;se ainda esta' no modo rampa,

				jp		nz, envia_ultimas ;deve-se evitar destruir os dados
				;coletados da orbita
				;		enquanto nao sejam lidos

	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;------	FAZ O TRATAMENTO DO BASTIDOR DE BPMs (Pos-booster)
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

				ld		a, 2
				ld		(tam_in), a
				ld		iy, buf_in+2
				ld		(ptdadosBPM), iy		;ponteiro de orbitas no inicio
				;busca no mapa de placas
				ld		a, (masc_BPM64)
				ld		(chave_BPM),a		;zera a variavel chaveadora

				call		bpm64_0
				jp		envia_ultimas

				;---------------------

nao_BPM64			ld		a, (libera_leitura)
				cp		0
				jp		z, nao_le
				ld		a, OPR_LEITURA
				ld		(operacao), a
				call		faz_ciclo		;faz leitura das placas

envia_ultimas			call		wdt

				ld		bc, buf_in		;parametros: ponteiro buffer de entrada
				ld		a, (tam_in)
				ld		h, a		;		tamanho
				ld		a, BLOCO_SAIDA		;		bloco 1 DPM
				call		envia_buffer		;transfere o buffer para DPM
				call		wdt

	;;;;	TESTA O COMANDO RECEBIDO...	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

nao_le				;JAMES 25/08
	;		ld		bc, buf_out	;parametros: ponteiro buffer de saida
	;		ld		a, BLOCO_ENTRADA ;		bloco de leitura da DPM
	;		call		recebe_buffer	;coleta o buffer de ajuste
				;JAMES 25/08

				ld		a, BLOCO_ENTRADA
				out		($03), a		;seleciona bloco de leitura da DPM
				in		a, ($00)		;carrega comando recebido

	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;;;;	VERIFICA COMANDO ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

				cp		CMD_NORMAL		;comando padrao
				jp		z, ini_ciclo
				cp		CMD_AJUSTE		;ajuste? (1)
				jp		z, labAjuste
				cp		CMD_MSGRECEB
				jp		z, labMsgReceb
				cp		CMD_BOOT		;Boot Remoto? (4)
				jp		z, labBoot
				cp		CMD_RSTRAMPA		;Limpeza de Rampa? (200)
				jp		z, labRstRampa
				cp		CMD_TXRAMPA		;transm. bloco de rampa? (201)
				jp		z, labTxRampa
				cp		CMD_HABRAMPA		;habilito rampa? (203)
				jp		z, labHabRampa
				cp		CMD_RAMPACIC		;habilito rampa ciclica? (208)
				jp		z, labRampaCic
				cp		CMD_ABORAMPA		;aborto de rampa? (204)
				jp		z, labAboRampa
				cp		CMD_AJUSTE2		;ajuste sem disparo do DAC?
				jp		z, labAjuste2
				cp		CMD_CONFIG		;e' comando de configuracao?
				jp		z, labConfig
				cp		CMD_DESTRAVA
				jp		z, labDestrava
				cp		CMD_CONPLCIC		;e' comando de configuracao de placas para ciclagem?
				jp		z, labConPlCic
				cp		CMD_HABCICLA
				jp		z, labHabCicla
				cp		CMD_ABOCICLA
				jp		z, labAboCicla
				cp		CMD_AJUSTE2
				jp		z, labAjuste2

				jp		ini_ciclo

	;;;;	COMANDO DE AJUSTE	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


labAjuste			ld		bc, buf_out		;parametros: ponteiro buffer de saida
				ld		a, BLOCO_ENTRADA ;		bloco de leitura da DPM
				call		recebe_buffer		;coleta o buffer de ajuste

				ld		a, (codigo_atual) ;prepara envio da mensagem
				ld		c, a		;		corrente
				ld		a, BLOCO_SAIDA
				call		Sinaliza		;indica fim do ajuste para o concentrador

				ld		a, BLOCO_ENTRADA
				ld		c, CMD_NORMAL
				call		Sinaliza		;limpa o byte de comando de ajuste

				ld		hl, buf_out

				ld		a, OPR_ESCRITA		;operacao de escrita nas placas
				ld		(operacao), a
				call		faz_ciclo		;executa a operacao


				ld		a, (em_deteccao)
				cp		0
				jp		nz, ini_ciclo

				ld		a, (buf_in)		;mostra novamente a prioridade
				cpl
				ld		(display), a		;da primeira placa

				jp		ini_ciclo

	;;;;	COMANDO DE BOOT (REMOTO)	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

labBoot:			ld		a, DISPLAY_4		;a:= "4" (com. de boot)
				ld		(display), a		;mostra no display a mensagem
				call		wdt

				ld		a, BLOCO_ENTRADA ;
				ld		c, CMD_NORMAL
				call		Sinaliza		;limpo o byte de comando de ajuste

				ld		a, BLOCO_SAIDA ;
				ld		c, CMD_BOOT
				call		Sinaliza		;limpo o byte de comando de ajuste

labBoota			jp		labBoota		;loop fechado para estouro do
				;"Watch Dog Time"


	;;;;	COMANDO DE LIMPEZA DE RAMPA	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

labRstRampa:			call		inic_rampa		;inicializa o mapa de rampa
				ld		a, $ff		;desmarca o numero de sequen-
				ld		(primeira), a		; cia para a primeira placa

				xor		a
				ld		(libera_leitura), a		;para de atualizar leitura
				ld		(int_em_trata), a

				ld		a, 1
				ld		(ja_confirmou_202), a ; garante que o primeiro
				; comando 201 sera' aceito

				ld		a, CMD_FIMRAMPA
				ld		(codigo_atual), a
				ld		a, BLOCO_SAIDA
				ld		c, CMD_FIMRAMPA		;envia comando rampa completada
				call		Sinaliza

				ld		a, (buf_in)		; mostra novamente a prioridade
				cpl
				ld		(display), a		;da primeira placa

				ld		a, BLOCO_ENTRADA
				ld		c, CMD_NORMAL		;limpa o byte recebido com
				call		Sinaliza		;		o comando

				ld		a, 32
				ld		(cdisp), a
				xor		a
				ld		(interrupt),a
				jp		ini_ciclo

	;;;;	COMANDO DE TRANSMISSAO DE BLOCO DE RAMPA	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

labTxRampa			call		wdt
				ld		a, (ja_confirmou_202)
				cp		0
				jp		z, ini_ciclo

				ld		bc, buf_trans		;parametros: ponteiro buffer de saida
				ld		a, BLOCO_ENTRADA ;		bloco de leitura da DPM
				call		recebe_buffer		;coleta o buffer de rampa
				;ja' foram desconsiderados
				;comando e tamanho do bloco

				push		ix		;salva o ponteiro

				ld		bc, buf_trans		;aponta para n. seq. da placa
				ld		hl, mapa_rampa

				ld		a, (cdisp)
				cp		32
				jp		nz, labTxRampa00
				ld		a, 1
labTxRampa00			rlca
				ld		(cdisp), a
				cpl
				ld		(display), a

labTxRampa0			ld		a, (hl)		;verifica o tipo no mapa de rampa
				cp		NAO_INSTALADA
				jp		z, labTxRampa1		;chegou ao fim de mapa?
				inc		hl
				ld		a, (bc)		;num. de sequencia da placa
				;		no bloco recebido
				cp		(hl)		;e' o mesmo no mapa de rampa?
				jp		z, labTxRampa01

				ld		de, TAM_MAPARAMPA-1		;nao, entao verifica a proxima
				add		hl, de		;		placa no mapa
				jp		labTxRampa0

labTxRampa01			inc		bc		;sim, entao significa que nao
				inc		bc		;e' o primeiro bloco recebido
				inc		bc		;para a placa e que as demais
				inc		bc		;informacoes ja' sao conhecidas

				ld		de, (ptbuf_rampa)
				;posiciona no inicio dos dados
				jp		labTxRampaTransf		;de rampa para a transferencia

labTxRampa1			ld		a, (primeira)		;ja' foi salvo o numero de
				cp		$FF		;sequencia da primeira placa?
				;(o numero de passos para essa
				; placa sera referencia para o
				; fim da rampa)
				jp		nz, labTxRampa10
				ld		a, (bc)		;nao, entao salva
				ld		(primeira), a		;

labTxRampa10			ld		a, (bc)		;numero de sequencia
				ld		ix, mapa_placa		;

labTxRampa11			dec		a		;busca no mapa de placas
				jp		z, labTxRampa2		;a n-esima placa
				ld		de, TAM_MAPAPLACA
				add		ix, de
				jp		labTxRampa11

				;------------------------------------
				;e' o primeiro bloco recebido
				;para a placa; sao entao sal-
				;vas as informacoes no mapa
labTxRampa2:			ld		a, (ix)		;de rampa:
				ld		(hl), a		; - o tipo da placa
				inc		hl		;
				ld		a, (bc)		;
				ld		(hl), a		; - o numero de sequencia
				inc		hl
				inc		bc
				ld		a, (bc)		; - o numero de pulsos
				inc		bc
				ld		(hl), a
				inc		hl

				ld		(hl), 4		;zera contador de pulsos 15/02/01
				inc		hl		;contador de pulso= 4 para que (era 3 ate' hoje)
				;mesmo para as placas de skip = 4
				;seja feito o ajuste logo no
				;primeiro pulso

				ld		a, (ix+2)		; - o endereco base da placa
				ld		(hl), a
				inc		hl
				ld		(hl), 0		;zera subpasso
				inc		hl
				ld		a, (bc)		; - o numero de passos (+sig.)
				ld		(hl), a
				inc		bc
				inc		hl
				ld		a, (bc)		; - o numero de passos (-sig.)
				ld		(hl), a
				inc		bc
				inc		hl
				ld		de, (ptbuf_rampa) ;resgata o ponteiro inicial
				;para o bloco de dados da
				;placa
				%rept		2
				ld		(hl), d		; - ponteiro inicial
				inc		hl		;		e
				ld		(hl), e		; - ponteiro movel
				inc		hl		;(iguais inicialmente)
				%endrept

				push		bc
				ld		bc, 8		;salta contador de pulsos
				add		hl, bc		;salta passo (2 bytes)
				;salta um quarto (2 bytes)
				;salta meio (2 bytes)
				;salta tres quartos (2 bytes)
				pop		bc

				ld		a, (ix)		;ve novamente o tipo
				cp		CODLCN12BMP
				jp		z,labTxRampa2_12b
				cp		CODLCN12BBP
				jp		z,labTxRampa2_12b
				cp		CODLCN16BMP
				jp		z,labTxRampa2_16b
				cp		CODLCN16B32MM
				jp		z,labTxRampa2_16b
				cp		CODLCN16BBP
				jp		z,labTxRampa2_16b

labTxRampa2_12b: ld		(hl), DOZE_BITS
				jp		labTxRampa3

labTxRampa2_16b: ld		(hl), DEZESSEIS_BITS

labTxRampa3:			inc		hl
				ld		(hl), NAO_INSTALADA ;marca o fim da tabela

labTxRampaTransf:
				call		wdt		;faz a copia do bloco
				;do buffer recebido para
				push		bc		;o buffer de rampa
				push		hl
				ld		h, b		;HL passa a apontar para
				ld		l, c		;		o buffer recebido
				ld		bc, 240		;BC tem o numero de bytes
				; para qual e' feita
				ldir		; a operacao:		(DE) <- (HL)

				pop		hl
				pop		bc
				ld		(ptbuf_rampa), de		;atualiza o ponteiro

				xor		a
				ld		(ja_confirmou_202), a ; aguardara'		a confirmacao
				; do comando 202
				ld		a, CMD_SOLRAMPA
				ld		(codigo_atual), a
				ld		a, BLOCO_SAIDA
				ld		c, CMD_SOLRAMPA
				call		Sinaliza		;solicita proximo bloco

				ld		a, BLOCO_ENTRADA
				ld		c, CMD_NORMAL		;limpa o byte recebido
				call		Sinaliza		;com o comando

				pop		ix
				jp		ini_ciclo

	;;;;	COMANDO DE HABILITACAO DE RAMPA CICLICA (208) ;;;;;;;;;;;;;;;;;;;;;;

labRampaCic:			ld		a, 1
				ld		(rampa_ciclica), a		;ajusta a variavel que
				;indica o repreparo
				;automatico das rampas
				;e faz a habilita normal
				;		(abaixo)

	;;;;	COMANDO DE HABILITACAO DE RAMPA (203) ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

labHabRampa:			call		wdt
				ld		hl, 0
				ld		(cont_passos), hl		;zera contador de passos

				ld		a, (mapa_rampa+6)		;numero de passos definido
				ld		(total_passos+1), a ;		para a rampa da
				ld		a, (mapa_rampa+7)		;		primeira placa
				ld		(total_passos), a

				ld		hl, mapa_rampa
labHabRampa1:			ld		a, (hl)
				cp		NAO_INSTALADA		;chegou ao fim da lista?
				jp		z, labHabRampa2

				inc		hl		;reinicializa ponteiros e
				inc		hl		;contadores no mapa de rampa
				inc		hl
				ld		(hl), 4		;zera contador de pulsos (=4) 15/02/01
				inc		hl
				inc		hl		; salta end. base
				ld		(hl), 0		;zera subpasso
				inc		hl		;
				inc		hl		;salta num. passos
				inc		hl		;
				ld		b, (hl)		;ponteiro inicial (+sig.)
				inc		hl
				ld		c, (hl)		;ponteiro inicial (-sig.)
				inc		hl
				ld		(hl), b		;move o ponteiro de dados
				inc		hl		;para o endereco inicial
				ld		(hl), c		;de rampa da placa
				ld		bc, TAM_MAPARAMPA - 11
				add		hl, bc
				jp		labHabRampa1		;proxima placa

labHabRampa2:			ld		a, BLOCO_ENTRADA
				ld		c, CMD_NORMAL
				call		Sinaliza		;limpo o byte recebido com
				;o comando

				ld		a, (rampa_ciclica)
				cp		0
				jp		nz, labHabRampa3
				ld		a, DISPLAY_SUBIDA ;a:= "rampa" habilitada
				jp		labHabRampa4
labHabRampa3:			ld		a,DISPLAY_Cicl		;a:=rampa ciclica habilitada

labHabRampa4:			ld		(display), a		;mostra no display
				call		wdt
				ld		a, INT_RAMPA
				ld		(interrupt),a

				xor		a
				ld		(int_em_trata), a ; sinaliza que ainda nao esta'
				; em tratamento
				ei		;habilita interrupcao
				jp		ini_ciclo


	;;;;	COMANDO DE ABORTO DE RAMPA	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

labAboRampa:			di		;desabilita interrupcao
				call		wdt

				ld		de, (cont_passos)
				ld		h, 3		;numero de parametros que
				;		serao passados
				ld		bc, buf_aux		;ponteiro de buffer
				ld		a, d
				ld		(bc), a		;contador de passos (+sig.)
				inc		bc
				ld		a, e
				ld		(bc), a		;contador de passos (-sig.)
				inc		bc
				ld		a, (mapa_rampa+3) ;contador de pulsos
				ld		(bc), a

				;residuo de pulsos desde o ultimo passo para a 1a. placa,
				; de forma que o numero de pulsos decorridos desde o inicio da
; rampa seja calculado por:
				;		residuo + (cont. passos * pulsos de espera)

				ld		a, CMD_HLTRAMPA		;sinaliza rampa abortada
				ld		(codigo_atual), a

labAboRampa1:			call		wdt
				ld		a, BLOCO_SAIDA		;bloco de escrita da DPM
				ld		h, 3		;tamanho
				ld		bc, buf_aux		;ponteiro de buffer
				call		envia_buffer		;envia a contagem de passos

				ld		a, BLOCO_ENTRADA		;fica enviando a contagem
				out		($03), a		;		e a mensagem de rampa
				in		a, ($00)		;		abortada
				cp		CMD_MSGRECEB		;ate' receber um comando
				jp		z, labAboRampa2		;de mensagem recebida
				cp		CMD_BOOT		;ou um reboot
				jp		z, labBoot		;
				jp		labAboRampa1		;continua enviando

labAboRampa2:			ld		a, BLOCO_ENTRADA
				ld		c, CMD_NORMAL		;limpa o byte recebido com
				call		Sinaliza		;		o comando

				ld		a, CMD_NORMAL
				ld		(codigo_atual), a
				ld		a, (tam_in)
				ld		h, a
				ld		a, BLOCO_SAIDA
				ld		bc, buf_in
				call		envia_buffer


				ld		a, (buf_in)		;mostra novamente a prioridade
				cpl		;		da primeira placa no
				ld		(display), a		;		display
				xor		a
				ld		(interrupt),a
				ld		(rampa_ciclica), a

				jp		ini_ciclo

	;;;;	COMANDO DE LEITURA DA CONFIGURACAO	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

labConfig:			ld		(codigo_atual), a ; sinaliza o estado atual
				ld		a, BLOCO_SAIDA
				ld		c, a		;limpa o byte recebido com
				call		Sinaliza		;		o comando
				ld		a, DISPLAY_2
				ld		(display), a		;mostra no display
				call		wdt

				ld		hl, mapa_placa ;mapa de configuracao
				ld		de, buf_aux		;buffer auxiliar de saida
				ld		b, 0		;contador de bytes

labConfig1:			ld		a, (hl)		;verifica o tipo da placa
				cp		NAO_INSTALADA		;chegou ao fim do mapa?
				jp		z, labConfig5
				inc		b		;conta mais quatro bytes
				inc		b		;para o envio
				inc		b		;
				inc		b		;
				ld		c, a		;salva o tipo

				%rept		3
				ld		a, (hl)		;copia tipo, tipo estendido e
				ld		(de), a		;endereco base
				inc		hl
				inc		de
				%endrept

				ld		a, c		;verifica se tipo e' Digiloco
				cp		CODDIG24CFD
				jp		z, confdig
				cp		CODDIG24CFO
				jp		z, confdig
				cp		CODDIGIINT
				jp		z, confdig
				cp		CODSTATFNT
				jp		z, confdig
				jp		labConfig2		;nao

confdig				dec		hl		;sim, e' da familia Digiloco
				ld		c, (hl)		;pega endereco base
				inc		c
				inc		c
				inc		c
				inc		c		;end. base + 4
				in		a, (c)		;le tipo da Digiloco
				inc		hl		;volta a posicao seguinte
				ld		(de), a		;salva no buffer auxiliar

labConfig2:			inc		de		;proxima placa
				inc		hl		;pula endereco de desvio
				inc		hl
				jp		labConfig1

labConfig5:			inc		b		;numero de bytes de conf.

				ld		(de), a		;marca fim da lista de conf.
				inc		de
				ld		a, b
				add		13		;acrescenta informacoes sobre
				;		a versao do software

				ld		bc, 13
				ld		hl, marca_versao
				ldir
				inc		a		;2 bytes para o contador
				inc		a		;de reset
				ld		b, a		;salvo tamanho em B

				ld		a, (contreset+1)
				ld		(de), a
				inc		de
				ld		a, (contreset)
				ld		(de), a
				ld		d, b		;salvo tamanho em D

labConfig6:			call		wdt
				ld		a, BLOCO_SAIDA ;bloco de escrita da DPM
				ld		h, d		;tamanho
				ld		bc, buf_aux		;ponteiro de buffer
				call		envia_buffer		;envia os dados da config.

				ld		a, BLOCO_ENTRADA ;fica enviando os dados
				out		($03), a
				in		a, ($00)
				cp		CMD_FIMCONFIG		;ate' receber um comando
				jp		z, labConfigfim		;de fim de config.,
				cp		CMD_AJUSTE		;de ajuste
				jp		z, labAjuste
				cp		CMD_BOOT		;ou um reboot
				jp		z, labBoot
				jp		labConfig6

labConfigfim:			ld		a, CMD_NORMAL
				ld		(codigo_atual), a
				ld		c, a
				ld		a, BLOCO_SAIDA
				Call		Sinaliza

				ld		a, (buf_in)		;mostra novamente a prioridade
				cpl		;		da primeira placa
				ld		(display), a		;

				jp		ini_ciclo

	;;;;	COMANDO DE MENSAGEM RECEBIDA	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

	; O sistema de controle, no caso de receber a comunicacao de um evento,
	; deve usar este comando para indicar que ja' a capturou.

labMsgReceb:			ld		a, CMD_NORMAL
				ld		(codigo_atual), a

				ld		(display_faixa), a

				ld		a, 1
				ld		(ja_confirmou_202), a ; aguardara'		a confirmacao

				ld		a, (buf_in)		; mostra novamente a prioridade
				cpl
				ld		(display), a		;da primeira placa

				ld		a, BLOCO_ENTRADA
				ld		c, CMD_NORMAL		;limpa o byte recebido com
				call		Sinaliza		;		o comando

				ld		a, BLOCO_SAIDA
				ld		c, CMD_NORMAL		;responde com Comando Normal
				call		Sinaliza

				jp		ini_ciclo

	;;;;;;;;;;;;;;;; COMANDO PARA DESTRAVAR A ATUALIZACAO DE LEITURA (207) ;;;;;;;;;;;;;;;;;;;
	; O programa volta a fazer as leituras das placas

labDestrava:			ld		a, 1
				ld		(libera_leitura), a

				xor		a
				ld		(rampa_ciclica), a

				ld		a, CMD_DESTRAVA
				ld		(codigo_atual), a
				ld		a, BLOCO_SAIDA
				ld		c, CMD_DESTRAVA		;envia comando rampa completada
				call		Sinaliza

				ld		a, (buf_in)		;mostra novamente a prioridade
				cpl		;
				ld		(display), a		;da primeira placa

				ld		a, BLOCO_ENTRADA ;
				ld		c, CMD_NORMAL		;limpa o byte recebido com
				call		Sinaliza		;		o comando
				jp		ini_ciclo

	;;;;;;;;;;;;;;;; COMANDO DE CONFIGURACAO DE CICLAGEM (6) ;;;;;;;;;;;;;;;;;;;

labConPlCic:			ld		bc, buf_out		;parametros: ponteiro buffer de saida
				ld		a, BLOCO_ENTRADA ;bloco de leitura da DPM
				call		recebe_buffer		;coleta o buffer de ajuste

				ld		hl, buf_out
				ld		a, DISPLAY_Cmin
				cpl
				ld		(display), a

				ld		a, OPR_ESCRITA		;operacao de escrita nas placas
				ld		(operacao), a
				call		wdt
				call		faz_ciclo		;executa a operacao
				ld		a, CMD_CCONFCIC		;prepara envio de confirmacao de configuracao
				ld		(codigo_atual),a

labConPlCica:			call		wdt
				ld		a, BLOCO_SAIDA
				push		hl		;
				push		bc		;
	;;;;;;;;;	ld	h,0	;tamanho
				call		envia_buffer		;
				pop		bc		;
				pop		hl		;
				ld		a,BLOCO_ENTRADA
				out		($03),a		;fico enviando CMD_CCONFCIC ate receber
				in		a,($00)		;o comando CMD_MSGRECEB
				cp		CMD_BOOT
				jp		z, labBoot
				cp		CMD_MSGRECEB
				jp		nz,labConPlCica

				ld		a, (buf_in)		;mostra novamente a prioridade
				cpl
				ld		(display), a		;da primeira placa
				jp		ini_ciclo

	;;;;;;;;;;;;;;;; COMANDO DE HABILITACAO DE CICLAGEM;;;;;;;;;;;;;;;;;;;

labHabCicla:			call		inic_cicla
				call		monta_tab
				ld		a, INT_CICLA
				ld		(interrupt),a
				ld		a,tot_pas_mais
				ld		(total_passos+1),a
				ld		a,tot_pas_menos
				ld		(total_passos),a
				xor		a
				ld		(num_pulsos),a		; zera num_pulsos atual
				call		wdt
				ld		hl,0
				ld		(cont_passos),hl		; zera contador de passos
				ld		hl, mapa_cicla
				inc		hl
labHabCicla1:			ld		a,(hl)
				cp		NAO_INSTALADA		; chegou ao fim da tabela?
				jp		z, labHabCicla2
				inc		hl
				inc		hl		;posiciono em contador de pulsos
				ld		(hl),0		;zero contador
				inc		hl
				inc		hl
				inc		hl
				inc		hl
				inc		hl
				jp		labHabCicla1
labHabCicla2:			ld		a,BLOCO_ENTRADA
				ld		c,CMD_NORMAL
				call		sinaliza
				ld		a,DISPLAY_H		;a:=ciclagem habilitada
				ld		(display),a		;mostra no display
				call		wdt
				ld		a,$00
				ld		(pont_d16b),a
				ld		a,$80
				ld		(pont_d16b+1),a		;carrego pont_d16b com o inicio do bloco de memo ext
				ld		a,$fe
				ld		(pont_d12b),a
				ld		a,$7f
				ld		(pont_d12b+1),a		;carrego pont_d12b com $7ffe
				ei
				jp		ini_ciclo

	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;;;;;;;;;;;;;;;;;;;;;;;; ABORTA CICLAGEM ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

labAboCicla:			di
				xor		a
				ld		(interrupt),a
				call		wdt
				ld		de,(cont_passos)
				ld		bc,buf_aux
				ld		a,d
				ld		(bc),a
				inc		bc
				ld		a,e
				ld		(bc),a
				ld		a, DISPLAY_U
				ld		(display),a
				ld		a,CMD_CICLABOR
				ld		(codigo_atual),a

LabAboCicla1:			call		wdt
				ld		a,BLOCO_SAIDA		; bloco de escrita da DPM
				push		hl
				push		bc
				ld		h,2		; tamanho
				ld		bc,buf_aux		; ponteiro para buffer
				call		envia_buffer		; envia a contagem de passos
				pop		bc
				pop		hl
				ld		a,BLOCO_ENTRADA		; fica enviando a contagem
				out		($03),a		; e a mensagem de ciclagem abortada
				in		a,($00)
				cp		CMD_MSGRECEB		; ate receber um comando
				jp		z,LabAboCicla2		; de mensagem recebida
				cp		CMD_BOOT		; ou um reboot
				jp		z,labAboCicla2
				jp		LabAboCiclA1		; continua enviando

labAboCicla2:			ld		a,BLOCO_ENTRADA
				ld		c,CMD_NORMAL		; limpa o byte recebido
				call		sinaliza		; o comando
				ld		a,CMD_NORMAL
				ld		(codigo_atual),a
				ld		c,a
				ld		a,BLOCO_SAIDA
				call		sinaliza
				ld		a,(buf_in)		; mostra novamente a prioridade
				cpl		; da primeira placa no display
				ld		(display),a
				in		a,($ff)		;seleciono ram_int
				jp		ini_ciclo

	;############## Comando de ajuste analogico sem disparar o conversor DAC ##############;

labAjuste2:			ld		a,1		;seto buffer sincrono o que fara com
				;que o DAC seja disparado somente por
				ld		(sincrono),a		;interrupcao trata_int3
				ld		a,INT_AJUSTE2
				ld		(interrupt),a		;carrego $77 no buffer interrupt para
				;fazer trata_int3.
				ei		;habilito int.
				jp		labAjuste

	;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

				org		$8000
rot_esp:			ds		256		;256 bytes para rotinas em ram

				org		$a000

	;*************************************************
	; Mapa com as placas detectadas no bastidor
	;
	;	tt xx bb el eh	tt xx bb el eh	...	$3F
	;*************************************************

mapa_placa
				%FOR		k = 1 to 32
				ds		1		; tt - tipo
				ds		1		; xx - tipo estendido
				ds		1		; bb - end. base da placa ($00, $08...)
				ds		2		; ee - end. de desvio para rotina
				%ENDFOR
fim_mapa_placa			db		NAO_INSTALADA		; fim de tabela
TAM_MAPAPLACA			equ		(fim_mapa_placa - mapa_placa)/32


mapa_rampa
				%FOR		k = 1 to 32
				db		NAO_INSTALADA		; tt - tipo (inicialmente 63 = vazio)
				ds		1		; nn - numero da placa na sequencia
				ds		1		; pp - numero de pulsos entre passos (1 ou 4)
				ds		1		; cc - contador de pulsos
				ds		1		; bb - end. base da placa ($00, $08...)
				ds		1		;substep-> indica qual substep sera realizado:
				ds		2		; ee - total de passos
				ds		2		; ii - ponteiro inicial para a rampa
				ds		2		; mm - ponteiro para mover na rampa
				ds		2		; valor atual de ponto (dado i)
				ds		2		; ponto interpolacao - meio
				ds		2		; ponto interpolacao - um quarto
				ds		2		; ponto interpolacao - tres quartos
				ds		1		; desvio para a rotina: 12 ou 16 (bits)
				%ENDFOR
fim_mapa_rampa			db		NAO_INSTALADA		; fim de tabela

TAM_MAPARAMPA			equ		(fim_mapa_rampa - mapa_rampa)/32

	;*************************************************
	; Buffer para rampa(s)
	;*************************************************

total_passos			ds		2		;total de passos para primeira
				;		placa		(ate' 4096)
cont_passos			ds		2		;numero de passos ja' dados para
				;		a primeira placa
primeira			ds		1		;numero de sequencia da primeira
				; placa com rampa

buf_aux				ds		256		;256 bytes para config. de placas
buf_trans			ds		256		;256 bytes para transf. rampa

buf_rampa			ds		17000		;ficam reservados 16 Kb para rampa
				;(max. 4096 passos em 16 bits)
ptbuf_rampa			ds		2
cont_pulsos			ds		1		;buffer auxiliar para contador de pulsos
quartoi				ds		2		;recebera valor de 1/4 da distancia entre
				;valor atual e proximo valor
meioi				ds		2		;recebera valor de 1/2 distancia entre
				;valor atual e proximo valor
tres_quartosi			ds		2		;recebera valor de 3/4 da distancia
				;entre valor aual e proximo valor
atuali				ds		2		;valor atual da rampa

	;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	;;;;;;;;;;;;;; BUFFERS PARA CICLAGEM ;;;;;;;;;;;;;;;;

bloco				ds		1 ; indica qual o bloco de memoria estendida
pont_d16b			ds		2 ; indica posicao dos dados p/ LCN16b nos blocos da memo ext
pont_d12b			ds		2 ; indica posicao dos dados p/ LCN12b nos blocos da memo ext
num_pulsos			ds		1 ; indica o numero de pulsos atual

	;;;;;;;;;;; MAPA DE CICLAGEM ;;;;;;;;;;;;;;;;;;;;;;;;
mapa_cicla
				%FOR		k = 1 to 32
				db		NAO_CICLAVEL		; ff - flag ident. tipo de ciclagem
				db		NAO_INSTALADA		; tt - tipo (inicialmente 63 = vazio)
				ds		1		; pp - numero de pulsos entre passos
				ds		1		; cc - contador de pulsos
				ds		1		; bb - end. base da placa ($00, $08...)
				ds		2		; ii - ponteiro inicial para a ciclagem
				%ENDFOR
				db		NAO_INSTALADA		; fim de tabela


	;*************************************************
	; buffers de trabalho
	;*************************************************

buf_out				ds		288		;256 bytes enviados para a maquina
buf_in				ds		288		;256 bytes lidos da maquina
buf_tmp				ds		32

display				ds		1		;conteudo indica valor no display
display_faixa			ds		1		;conteudo indica valor no display das faixas
cdisp				ds		1

prioridade			ds		1		;prioridade da placa em questao
ciclo				ds		1		;contador do ciclo (para prioridade)
base				ds		1		;conteudo indica end. base de
end_contr			ds		1		;endereco do controlador udc3000

				;porta para a placa
libera_leitura			ds		1
tipo_opr			ds		1		;indicador (auxiliar) para
				;o tipo de operacao
operacao			ds		1		;conteudo indica tipo de operacao
				;da procedure leitura
				;OPR_LEITURA-leitura das placas
				;OPR_ESCRITA-ajuste das placas
				;OPR_PROXIMA-proxima placa
				;OPR_BOOT-zeramento no boot

sincrono			ds		1		;indica se o ajuste analogico das LCN's 12b e/ou 16b
				;sera realizado atraves de interrupcao ou nao.
				;Se sincrono=1 entao sera realizado por interrupcao.
				;Se sincrono=0 entao nao sera realizado por int.
				;Durante o boot, sincrono e resetado.

codigo_atual			ds		1
comando				ds		1
tam_out				ds		1		;tamanho da mensagem p/ controle
tam_in				ds		1		;tamanho da mensagem do controle
tamanho				ds		1
end_monitory			ds		1		;endereco base para uma placa BPM_Y
end_monitorp			ds		1		;endereco base para uma placa BPM_P

	;---------------------------- variaveis ligadas ao BPM64
n_loopBPM			ds		1
ptdadosBPM			ds		2
bastidorBPM			ds		1
modorampaBPM			ds		1
endbaseBPM1			ds		1
endbaseBPM2			ds		1
masc_BPM64			ds		1
n_integBPM64			ds		1
chave_BPM			ds		1
conta_pulsosBPM ds		1
conta_passosBPM ds		1
pulsos_entreBPM ds		1
integracoesBPM			ds		1
cont_vez			ds		1
	;--------------------------- variaveis ligadas `a deteccao de corrente

endbase_relboo			ds		1 ;endereco base para o relogio do booster
endbase_atrasboo		ds		1 ;endereco base para a placa atraso geral booster
endbase_fotsinc			ds		1 ;end. base da contadora passo rampa booster
endbase_fotdcct			ds		1 ;end. base da contadora DCCT
endbase_relrsinc		ds		1 ;end. base do relogio de rampa
endbase_loconDcct ds		1 ;end. base da locon DCCT
rampa_ciclica			ds		1		;sinaliza se esta' (1) ou nao (0) no
				;modo de rampeamento ciclico automatico
				;do booster
em_deteccao			ds		1		;sinaliza se esta' (1) ou nao (0) no
				;processo de deteccao de corrente
				;para rampeamento do booster
passoEJECrampa			ds		2
ultpassorampaBOO		ds		2
corrlimite			ds		2
delayDCCT			ds		3

	;TABELA DE VELOCIDADES DE RAMPA DO BOOSTER ---
tabvelocBOO
				%FOR		k = 1 to 10
				ds		1		; #passos (+sig.)
				ds		1		; #passos (-sig.) em que permanece na taxa
				ds		1		; taxa repet. (+sig.)
				ds		1		; taxa repet. (-sig.)
				%ENDFOR

indtabveloc			ds		2
faixaveloc			ds		1
	;--------------------------------------------------------------------------

conta_placa			ds		1
prior_p				ds		1

ix_relogboo			ds		2		;status do relogio do booster
ix_monitorx			ds		2		;aponta posicao no buffer para BPM_X
ix_monitory			ds		2		;aponta posicao no buffer para BPM_Y
ix_monitorp			ds		2		;aponta posicao no buffer para BPM_P

atraso_ciclos			ds		1		;numero de intervalos de 50ms entre
				;		duas rampas ciclicas do booster
atraso_espalha			ds		2		;atraso de espalhamento para booster
				;		quando de rampas ciclicas


lsd				ds		1		;buffer para dado -sig para placa CDLCNADD
hsd				ds		1		;buffer para dado +sig para placa CDLCNADD
num_ciclos			ds		1		;numero de ciclos de leitura para inversao do ponto	;no display.
interrupt			ds		1		;indica qual int.mascaravel sera realizada
				;		INT_CICLA->ciclagem, INT_RAMPA->rampeamento, INT_AJUSTE2->dispara DAC
time_out			ds		1		; time out da rotina DRU(data ready UDC3000)
flag				ds		1
contreset			db		$0		;contador para o numero de reset-1
				db		$0		;ocorridos
espaco				ds		20
temp				ds		1		;rascunho temporario

leitY				ds		2
sinalizador			ds		1
relogboo_cego			ds		1

int_em_trata			ds		1
pulsos_durante_trata		ds		1
ja_confirmou_202		ds		1
ciclos100us			ds		1

nplc				ds		1
respIX232			ds		2
pontoIX232			ds		2
qtcarac232			ds		1
pende232			ds		1
leit232				ds		TOTCARAC232
time_out232			ds		1		; time out da rotina DR(data ready)
base232				ds		1
tentou232			ds		2
tentativas232			ds		2

dispfotcontinuo			ds		1

posantinteg			ds		2
leittemp			ds		2
ninteg				ds		2

iniseries			ds		2 * 1000 * MAXSERIESINTEG

tabseries			
				%FOR		k = 1 to MAXSERIESINTEG
				db		NAO_INSTALADA			    ; modulo+offset da placa 	
				dw		(iniseries + 2 * (k-1) * MAXINTEG12BITS)    ; posicao do primeiro ponto
				dw		$9999 	  	            ; posicao do ponto atual
				dw		(iniseries + 2 * k * MAXINTEG12BITS - 2)    ; posicao do ultimo ponto
				%ENDFOR
				db		NAO_INSTALADA

				
				END

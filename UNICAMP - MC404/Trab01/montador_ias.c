/***************************************************/
/* Gustavo Ciotto Pinton RA 117136		   */
/***************************************************/
#include "montador_ias.h"

/* Recebe como parâmetro uma string contendo um valor em qualquer base e retorna 
    no outro parametro **result o resultado do valor em hexa em 40bits. */
void get_char_value(char *word, char** result) {
      long int return_v;     
      long long int a = 0;
      /* base inicial = 10 e flag isNegative eh falsa */
      int base = 10, isNegative = 0;
      char _aux[20], *cadeia = word;
      
      /* se jah estiver em hexa basta adicionarmos 0 à esquerda, caso seja necessario. */
      if (strchr(word,'x')) {
	  sprintf(_aux,"%s", word+2); /* Elimina o '0x' */
	  int size = strlen(_aux); /* Verifica quantos digitos ja existem. */
	  
	  while (strlen(*result) < 10 - size) /* Adiciona 0 necessarios */
	     strcat(*result,"0");
	  strcat(*result,_aux);
	  return;
      }
	
      else if (strchr(word,'h')) /* Tratamento semelhante mas com a simbologia de hexa na forma de 'h' */{
	  word[strlen(word) - 1] = '\0'; /* Elimina 'h' */
	  sprintf(_aux,"%s", word);
	  int size = strlen(_aux);
	  
	  while (strlen(*result) < 10 - size)
	     strcat(*result,"0");
	  strcat(*result,_aux);
	  return;
      }
      /* convertemos bases 2 e 8 para decimal para depois convertermos para hexa*/
      else if (strchr(word,'b')) { base = 2; cadeia = word +2; } 
      else if (strchr(word,'o')) { base = 8; cadeia = word +2; }
      else { /* Numero decimal */
	  base = 10;         
	  /* Verifica se o numero eh negativo */
	  if (strchr(word,'-')) {
	      isNegative = 1; /* Se possuir '-' eh negativo. Devemos guardar essa informacao. */
	      cadeia = word +1; /* Por enquanto soh trabalhamos com o modulo do numero */
	  }
      }
      
      /* Convertemos para decimal */
      return_v = strtol(cadeia, NULL, base);
      
      if (isNegative) return_v = ~return_v +1; /* se for negativo, convertemos conforme a formula para complemento de 2. */
      
      a += return_v;
      
      sprintf(_aux,"%016llX", a); 
      strncpy(*result,_aux+6,10);/* Escrevemos adequadamente para a saida */

}

/* Dado uma string, a funcao retorna o valor correspondente. Essa funcao soh funciona para opcodes ou
    enderecos de memoria, uma vez que um int, que eh o retorno, soh comporta 32bits, insuficientes 
    para uma palvra completa do ias. Se quisermos 40bits devemos chamar a funcao acima. */
int get_number(char *word) {
      int return_v;
      /* converte para bases de acordo com a palavra. */
      if (strchr(word,'x')) /*0x*/{
	  return_v = strtol(word + 2, NULL, 16);
	  
      }
      else if (strchr(word,'h')) /* FFh */ {
	  word[strlen(word) - 1] = '\0';
	  return_v = strtol(word, NULL, 16);
      }
      /* 0b */
      else if (strchr(word,'b')) return_v = strtol(word + 2, NULL, 2);
      /* 0o */
      else if (strchr(word,'o')) return_v = strtol(word + 2, NULL, 8);
      else return_v = strtol(word, NULL, 10);
      return return_v;
}

/* Verfica se a palavra passada como parametro eh um rotulo, e caso seja, retorna sua posicao de memoria.
    Tambem ativa a falg isLabel caso seja encontrado um rotulo. Retorna falso ao contrario. */
int is_Label(char* word, label_table t, int* isLabel) {
	int i;
	/* Procura no vetor de rotulos */
        for (i = 0; i < t.count; i++)
	  if (!strcmp(word, t.labels[i].name)) {
	      *isLabel = 1;
	      return t.labels[i].position;
	  }
	return 0;
}

/* Dada  uma palavra, procura-se nos vetores de rotulos e constantes e caso seja encontrada, retorna-se o valor
    corretamente. As flags isLabel e isContraint sao ativadas quando detecta-se a ocorrencia de um desses tipos. 
    Caso nao seja achada, retorna seu valor numerico. */
int get_label_value(char* word, label_table t, set_table s, int* isLabel, int* isContraint) {
    int i;
    /* Procura no vetor de rotulos */
    for (i = 0; i < t.count; i++)
	if (!strcmp(word, t.labels[i].name)) {
	    *isLabel = 1;
	    return t.labels[i].position;
	}
	
    /* Procura no vetor de constantes (.SET) */
    for (i = 0; i < s.count; i++)
	if (!strcmp(word, s.constraint[i].name)){
	    *isContraint = 1;
	    return s.constraint[i].mem;
	}
    return get_number(word);
}

/* Escreve no arquivo de saida o resultado da montagem. Recebe arquivos de entrada e saida,
    e vetores de rotulo e constantes. */
void print_file(char* fileFrom, char* fileTo, label_table t, set_table s){
      FILE* _fileFrom = fopen(fileFrom,"r");
      FILE* _fileTo = fopen(fileTo,"w");
      char word[20];
      int position_count= 0;

      while (!feof(_fileFrom)) {
	    fscanf(_fileFrom," %s ", word);
	    /* Leu comentario. Le toda a linha */
	    if (strchr(word,'@')) {
		char character = fgetc(_fileFrom);
		while (character != '\n')  character = fgetc(_fileFrom);
	    }

	    else if (strchr(word,'.')){ /* Eh diretiva, ou seja foi encontrado um '.' */
		int d = get_directive(word), isLabel = 0, isContraint = 0;
		/* Variaveis auxiliares */
		char aux[20];
		char *_aux_string = (char*) malloc(10*sizeof(char));
		int _num, _aux, _value;
		/* Le primeiro parametro */
		fscanf(_fileFrom," %s ",word);
		switch (d) {
		      /* .org soh precisamos atualizar contador */
		      case ORG 	: position_count = get_number(word)*2; break;

		      /* verificamos se preecisamos alinhar mapa de memoria e se ha a necessidade de imprimirmos
			 00000 a direita de uma linha */
		      case ALIGN: _num = get_number(word);
				  if (position_count%(2*_num)) {
				      if (position_count%2) /* imprime zeros caso esteja na direita */
					fprintf(_fileTo,"%05X\n",0);
				      position_count = position_count/(2*_num)*2 + 2*_num;

				  }
				  break;
		      /* Se eh um .WORD, verificamos se eh um rotulo ou um valor */
		      case WORD	: _value = is_Label(word, t,&isLabel);
				  /* se for rotulo, temos que dividir endereço de memoria por dois */
				  if (isLabel)	sprintf(aux,"%010X", _value/2);
				  else {
				      get_char_value(word, &_aux_string);
				      sprintf(aux,"%s", _aux_string);  /* escreve na variavel auxiliar para depois escrevermos no arquivo */
				  }

				  fprintf(_fileTo,"%03X %s\n",position_count/2, aux);
				  position_count += 2; break;/* atualiza contador */

		      case WFILL: word[strlen(word)-1] = '\0'; /* ignora-se a ','*/
				  _num = get_number(word);  /* quantidade de iteracoes */

				  fscanf(_fileFrom," %s ",word);
				  /* transforma valor lido adequadamente para impressao */
				  get_char_value(word,&_aux_string);
				  /* verifica se um rotulo, constante ou valor */
				  _value = get_label_value(word, t, s, &isLabel, &isContraint);

				  for (_aux = 0; _aux < _num; _aux++){
				      /* trata cada caso individualmente */
				      if (isContraint)	{
					  sprintf(word,"%d", _value); /* passa valor da constante para um string, a fim de utilizarmos a
									  funcao get_char_value */
					  get_char_value(word,&_aux_string);
					  sprintf(aux,"%s", _aux_string);
				      }
				      else if (isLabel)	sprintf(aux,"%010X", _value/2);
				      else 		sprintf(aux,"%s", _aux_string);
				      /* escreve no arquivo */
				      fprintf(_fileTo,"%03X %s\n",position_count/2, aux);
				      position_count += 2;
				  }
				  break;
				  /* nao faz nada, soh le valor */
		      case SET	: fscanf(_fileFrom," %s ",word);
				  break;

		      default 	: printf("Erro");
		}
		free(_aux_string );
	    }
	    /* se nao for rotulo, soh pode ser instrucao */
	    else if (!strchr(word,':')){
		/* Verificamos 'familia' de instrucao */
		int _option = get_instruction(word), opcode,  isLabel = 0, _val, isContraint = 0;
		/* Variaveis auxiliares */
		char _aux[20], _aux2[20];
		switch (_option) {
		  case M_INSTRUCTION	: opcode = get_instruction_opcode(word, position_count%2);
					  fscanf(_fileFrom," %s ",word); /* OBTER VALOR ENTRE M() */
					  strncpy(_aux, word+2, strlen(word));
					  _aux[strlen(word)-3] = '\0';
					  /* obtem valor de constante, rotulo ou valor */
					  _val = get_label_value(_aux,t,s,&isLabel,&isContraint);
					  break;

		  /* Nao ha posicoes de memoria para serem lidas */
		  case NO_M_INSTRUCTION	: opcode = get_instruction_opcode(word, position_count%2);
					  _val = 0;
					   break;
		  /* Devemos verificar se a instrucao refere-se a direita ou esquerda */
		  case LR_INSTRUCTION	: fscanf(_fileFrom," %s ",_aux2); /* OBTER VALOR ENTRE M() */
					  sprintf(_aux, "%s", _aux2+2);
					  _aux[strlen(_aux)-1] = '\0';
					  /* obtem valor de constante, rotulo ou valor */
					  _val = get_label_value(_aux, t,s,&isLabel, &isContraint);
					  /* obtem valor correto da instrucao */
					  opcode = get_instruction_opcode(word, _val%2);
					  break;
		}

		if (isLabel) _val = _val/2; /* Se for um rotulo, o valor deve ser divido por 2, afinal
						ele representa uma posicao de memoria */

		if (position_count%2)  /* Verifica se ha a necessidade de escrever a posicao de memoria atual. */
		    fprintf(_fileTo,"%02X%03X\n",opcode,_val);
		else fprintf(_fileTo,"%03X %02X%03X",position_count/2,opcode, _val);

		position_count++;
	    }
      }

      /* Verifica se a necessidade de completarmos fim de arquivo com 0 (termicou na direita */
      if (position_count%2) fprintf(_fileTo,"%05X\n",0);

      fclose(_fileFrom);
      fclose(_fileTo);
      return;
}

/* funcao responsavel pela montagem do arquivo. Une  todos processos. */
void mount(char* fileFrom, char* fileTo) {
    set_table s_table;
    label_table l_table = build_labels(fileFrom, &s_table);

    print_file(fileFrom, fileTo, l_table, s_table);
    free_tables(s_table, l_table);
}

/* funcao responsavel por imprimir na tela vetores de rotulos e constantes */
void print_arrays (set_table s_table, label_table l_table) {
    int i;
    for (i = 0; i< l_table.count; i++) {
	printf("'%s' %d", l_table.labels[i].name, strlen(l_table.labels[i].name));
	if (l_table.labels[i].position%2)
	    printf(" %x dir %d\n", l_table.labels[i].position/2,  l_table.labels[i].position);
	else printf(" %x esq %d\n", l_table.labels[i].position/2,  l_table.labels[i].position);
    }

    for (i = 0; i< s_table.count; i++) {
	printf("%s - %x %d\n", s_table.constraint[i].name,s_table.constraint[i].mem,s_table.constraint[i].mem);
    }
}

/* As instrucoes foram divididas em tres grupos de acordo com a quantidade de parametros necessarios e se ha necessidades da
    distincao entre esquerda e direita */
int get_instruction(char* name) {
    /* Instrucoes que soh possuem um parametro */
    if ( (!strcmp(name, "LOADMQMem")) || (!strcmp(name,"STOR")) || (!strcmp(name,"LOAD")) || (!strcmp(name,"LOADNeg"))
	  || (!strcmp(name, "LOADMod")) || (!strcmp(name, "ADD")) || (!strcmp(name, "ADDMod")) || (!strcmp(name, "SUB")) || (!strcmp(name, "SUBMod"))
	    || (!strcmp(name, "DIV")) || (!strcmp(name, "MUL")) )
	return M_INSTRUCTION;

    /* Instrucoes que nao tem parametro. */
    if ( (!strcmp(name, "LOADMQ")) || (!strcmp(name, "RSH")) || (!strcmp(name, "LSH")) )
	return NO_M_INSTRUCTION;

    /* Instrucoes que precisam saber se determinada instrucao esta na direita ou esquerda */
    if ( (!strcmp(name, "JUMP")) || (!strcmp(name, "JUMPPos")) || (!strcmp(name,"STORAddr")) )
	return LR_INSTRUCTION;

    return -1;
}

/* Retorna os opcodes das instrucoes. Quando necessitamos saber se esta na direita ou esquerda, utilizamos o parametro right, */
int get_instruction_opcode(char* name, int right) {
    if (!strcmp(name, "LOADMQ")) return 10;
    if (!strcmp(name, "LOADMQMem")) return 9;
    if (!strcmp(name,"STOR")) return 33;
    if (!strcmp(name,"LOAD")) return 1;
    if (!strcmp(name,"LOADNeg")) return 2;
    if (!strcmp(name, "LOADMod")) return 3;
    if (!strcmp(name, "ADD")) return 5;
    if (!strcmp(name, "ADDMod")) return 7;
    if (!strcmp(name, "SUB")) return 6;
    if (!strcmp(name, "SUBMod")) return 8;
    if (!strcmp(name, "DIV")) return 12;
    if (!strcmp(name, "MUL")) return 11;
    if (!strcmp(name, "RSH")) return 21;
    if (!strcmp(name, "LSH")) return 20;
    if (!strcmp(name, "JUMP")) {
	if (!right) return  13;
	else return 14;
    }
    if (!strcmp(name, "JUMPPos")) {
	if (!right) return  15;
	else return 16;
    }
    if (!strcmp(name, "STORAddr")) {
	if (!right) return  18;
	else return 19;
    }
    return -1;
}


/* Função responsável pela primeira fase da montagem. Lê do arquivo e retorna um vetor com as
   posicoes dos rotulos. Constroi tambem vetor de constantes. */
label_table build_labels(char *filename, set_table* constraints) {
      FILE* file = fopen(filename,"r"); /* Abre arquivo para leitura */
      label_table lbl;
      lbl.count = 0;

      char word[20];
      int position_count= 0; /* Contador da posicao da memoria. Numero par esta na esquerda e impar, na direita. */
      constraints->count = 0;

      while (!feof(file)) { /* Enquanto nao eh fim de arquivo */
	    fscanf(file," %s ", word);

	    if (strchr(word,'@')) {  /* Verifica se nao eh comentario. Caso seja, le ate fim da linha. */
		char character = fgetc(file);  /* Verifica se tenho que ler ate um \n */
		while (character != '\n')  character = fgetc(file);
	    }

	    else if (strchr(word,'.')){  /* Presença de ponto seginifica diretiva. */
		int d = get_directive(word), _num;
		set s;
		fscanf(file," %s ",word); /* Le tipo da diretiva e, de acordo com ela, verifica se ha a necessidade de ler novamente. */
		switch (d) {
		      /* Caso seja .org  só precisamos atualizar contador de posicoes. */
		      case ORG 	: position_count = get_number(word)*2; break;

		      /* Se for .ALIGN, verficamos se precisamos atualizar contador ou nao. */
		      case ALIGN: _num = get_number(word);
				  if (position_count%(2*_num)) position_count = position_count/(2*_num)*2 + 2*_num;
				  break;

		      /* Se .word, soh precisamos incrementar o contador de posicao em uma palavra. */
		      case WORD	: position_count += 2; break;

		      /* Caso. wfill, precisamos somente ler a quantidade de palavras utilizadas e somar com o contador. */
		      case WFILL: word[strlen(word)-1] = '\0'; /* le primeira parte, eliminando a virgula. */
				  _num = get_number(word); /* adquire o valor correspondente a string */
				  position_count += 2*_num;
				  fscanf(file," %s ",word); break; /* le valor */

		      /* Caso .set, criamos um novo registro no vetor de constantes com seu nome e valor. */
		      case SET	: s.name = (char*) malloc((strlen(word)+1)*sizeof(char));
				  strcpy(s.name, word);
				  fscanf(file," %s ",word); s.mem = get_number(word);
				  constraints->constraint[constraints->count++] = s; /* Adciona no vetor */
				  break;
		      default 	: printf("Erro");
		}
	    }
	    /* Se for um rotulo, procedemos igualmente ao .set: criamos um novo registro no vetor de rotulos. */
	    else if (strchr(word,':')) {
		label l;
		l.name = (char*) malloc((strlen(word))*sizeof(char));
		strncpy(l.name, word, strlen(word));
		l.name [strlen(word)-1] = '\0';
		l.position = position_count;
		lbl.labels[lbl.count++] = l;
	    }

	    else { /* Se for instrucao, soh precisamos verificar quando eh LSH RSH ou LOADMQ para ler ourta palvra ou nao. */
		if (strcmp(word,"LSH") && strcmp(word,"RSH") && strcmp(word,"LOADMQ")) fscanf(file," %s ",word);
		position_count++;
	    }
      }
      /* Fecha arquivo de leitura. */
      fclose(file);
      return lbl;
}

/* Retorna o tipo de directiva, dada uma string. Retorna -1 caso não seja válida. */
int get_directive(char* name) {
      if (!strcmp(name,".wfill"))return WFILL;
      if (!strcmp(name,".org"))  return ORG;
      if (!strcmp(name,".align"))return ALIGN;
      if (!strcmp(name,".word")) return WORD;
      if (!strcmp(name,".set")) return SET;
      return -1;
}

/* Libera espaços de memória utilizados */
void free_tables(set_table s, label_table l) {
    int i;
    for (i = 0; i< s.count; i++)
	 free(s.constraint[i].name);
    for (i = 0; i< l.count; i++)
	 free(l.labels[i].name);
}

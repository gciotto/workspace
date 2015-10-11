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
	
    /* Procura no vetor de con
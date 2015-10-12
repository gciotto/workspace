import java.util.StringTokenizer;
import java.util.Stack;
import java.util.EmptyStackException;
import java.util.HashMap;

/**
 * Analyseur d'expressions arithmétiques simples.
 */
public class ExpressionParser {
    private String expression;
    private Stack<Character> operators;
    private Stack<Expression> arguments;
    private State curState;
    private static HashMap<Character,OperatorFactory> factories;

    static {
        factories = new HashMap<Character,OperatorFactory>();
        factories.put('+', new AdditionFactory());
        factories.put('-', new SubtractionFactory());
        factories.put('*', new ProductFactory());
        factories.put('/', new DivisionFactory());
    }   
    /** Crée un analyseur syntaxique sur la chaine <code>expression</code>. */
    public ExpressionParser(String expression) {
        this.expression = expression;
        this.operators = new Stack<Character>();
        this.arguments = new Stack<Expression>();
    }

    private enum State {
        ExpectArgOrPar, ExpectOpOrPar;
    }

    private void enterState(State cur) {
        this.curState = cur;
    }

    private State currentState() {
        return this.curState;
    }

    /**
     * Analyse la chaîne de caractères et rend l'expression correspondante.
     */
    public Expression parse() {
        StringTokenizer toks = new StringTokenizer(this.expression, "+-*/() ", true);
        enterState(State.ExpectArgOrPar);
        String lastTok = "** Start of expression **";

        while (toks.hasMoreTokens()) {
            String tok = toks.nextToken();
            if (tok.charAt(0) == ' ') { // on ignore les espaces
                continue;
            }
            // Si le premier caractère est un chiffre, on l'interprète
            // comme une constante numérique de type double
            if (Character.isDigit(tok.charAt(0))) {
                if (currentState() != State.ExpectArgOrPar) {
                    System.err.println(
                            "# Erreur : une constante numérique "
                            + tok 
                            + " n'est pas autorisée après "
                            + lastTok);
                    System.exit(1);
                }
                lastTok = tok;

                try {
                    double val = Double.parseDouble(tok);
                    this.arguments.push(new Number(val));
                    enterState(State.ExpectOpOrPar);
                } catch (Exception e) {
                    System.err.println("# Erreur : " + e.toString());
                    System.exit(1);
                }
                continue;
            }
            // Dans les autres cas (variable, opérateur ou parenthèse), le
            // lexème ne doit contenir qu'un seul caractère
            if (tok.length() > 1) {
                System.err.println(
                        "# Erreur : noms de variables et d'opérateurs sur 1 caractère ("
                        + tok + ")"
                );
                System.exit(1);
            }
            // S'il s'agit d'une lettre, on a une variable
            if (Character.isLetter(tok.charAt(0))) {
                if (currentState() != State.ExpectArgOrPar) {
                    System.err.println(
                            "# Erreur : une variable "
                            + tok 
                            + " n'est pas autorisée après "
                            + lastTok);
                    System.exit(1);
                }
                lastTok = tok;
                this.arguments.push(new Variable(tok.charAt(0)));
                enterState(State.ExpectOpOrPar);
                continue;
            }
            // Sinon, il s'agit d'un opérateur ou de parenthèses
            switch (tok.charAt(0)) {
                case '(':  /* ) parenthèse correspondante pour l'éditeur */
                    if (currentState() != State.ExpectArgOrPar) {
                        System.err.println(
                                "# Erreur : une parenthèse ouvrante "
                                + tok 
                                + " n'est pas autorisée après "
                                + lastTok);
                        System.exit(1);
                    }
                    lastTok = tok;
                    // On empile le caractère sur la pile des opérateurs
                    this.operators.push(tok.charAt(0));
                    enterState(State.ExpectArgOrPar);
                    break;

                    /*(*/ case ')':
                        // lorsqu'on rencontre une parenthèse fermante, il faut dépiler
                        // tous les opérateurs jusqu'à rencontrer la parenthèse ouvrante
                        // correspondante.
                        boolean missing_opening_delim = true;

                        if (currentState() != State.ExpectOpOrPar) {
                            System.err.println(
                                    "# Erreur : une parenthèse fermante "
                                    + tok 
                                    + " n'est pas autorisée après "
                                    + lastTok);
                            System.exit(1);
                        }
                        lastTok = tok;
                        while(!this.operators.empty()) {
                            char c = this.operators.peek().charValue();
                            if (c == '(') {         /* ) */
                                this.operators.pop();  // on a trouvé l'ouvrant correspondant
                                missing_opening_delim = false;
                                break;
                            } else {
                                // on construit l'objet représentant l'opérateur
                                buildOperator(findFactory(c));
                                // et on supprime le nom de l'opérateur de la pile
                                this.operators.pop();
                            }
                        }
                        if (missing_opening_delim) {
                            System.err.println("# Erreur, parenthèse ouvrante manquante.");
                            System.exit(1);
                        }
                        enterState(State.ExpectOpOrPar);
                        break;

                    default: // Ca ne peut être qu'un opérateur
                        OperatorFactory current = findFactory(tok.charAt(0));
                    if (current == null) {
                        System.err.println("# Erreur, opérateur '" + tok + "' inconnu.");
                        System.exit(1);
                    }
                    if (currentState() != State.ExpectOpOrPar) {
                        System.err.println(
                                "# Erreur : un opérateur "
                                + tok 
                                + " n'est pas autorisé après "
                                + lastTok);
                        System.exit(1);
                    }
                    lastTok = tok;
                    // Par défaut, l'algo d'analyse rend les opérateurs associatifs à 
                    // droite. Il faut donc traiter le cas des opérateurs non-associatifs
                    // à droite.
                    // Il faut aussi tenir compte de la priorité des opérateurs.
                    while (!this.operators.empty()) {
                        OperatorFactory top = findFactory(this.operators.peek());
                        if (top == null) {
                            break;
                        }
                        if (((top.priority() == current.priority())
                                && top.isNotRightAssociative())
                                || (top.priority() > current.priority())) {
                            buildOperator(top);
                            this.operators.pop();
                        } else {
                            break; // while (!empty)
                        }
                    }
                    // On empile le nouvel opérateur
                    this.operators.push(tok.charAt(0));
                    enterState(State.ExpectArgOrPar);
                    break;
            }
        }
        // Toute la chaîne a été analysée, il faut donc vider la pile des
        // opérateurs pour construire l'expression qui doit être le seul
        // élément de la pile des arguments à la fin.
        while (!this.operators.empty()) {
            OperatorFactory top = findFactory(this.operators.peek());
            buildOperator(top);
            this.operators.pop();
        }
        if (this.arguments.size() != 1) {
            System.err.println("# Erreur de syntaxe.");
            System.exit(1);
        }
        return this.arguments.pop();
    }

    /** Rend l'UsineAOperateurs associée au symbole <code>key</key>. */
    private static OperatorFactory findFactory(Character key) {
        return factories.get(key);
    }

    /**
     * Construit un opérateur binaire correspondant à <code>op</code>, en
     * prenant ses arguments dans la pile d'arguments.
     */
    private void buildOperator(OperatorFactory op) {
        Expression arg1 = null;
        Expression arg2 = null;
        // On récupère les arguments dans la pile des arguments
        // Le sommet de la pile est le deuxième argument (il a été
        // empilé après le premier argument).
        try {
            arg2 = this.arguments.pop();
            arg1 = this.arguments.pop();
        } catch (EmptyStackException e) {
            System.err.println("# Erreur : argument manquant pour '" + op +"'.");
            System.exit(1);
        }

        // On crée l'opérateur correspondant et on l'empile
        this.arguments.push(op.build(arg1, arg2));

    }
}

/** Interface des "usines" à opérateurs binaires. */
interface OperatorFactory {
    /** Construit un opérateur binaire à partir de ses arguments. */
    public BinaryOperator build(Expression arg1, Expression arg2);
    /** Indique si l'opérateur n'est pas associatif à droite. */
    public boolean isNotRightAssociative();
    /** Rend la priorité de l'opérateur. */
    public int priority();
}

class AdditionFactory implements OperatorFactory {
    @Override
	public BinaryOperator build(Expression arg1, Expression arg2) {
        return new Addition(arg1, arg2);
    }

    @Override
	public boolean isNotRightAssociative() {
        return false;
    }

    @Override
	public int priority() {
        return 1;
    }
}

class SubtractionFactory implements OperatorFactory {
    @Override
	public BinaryOperator build(Expression arg1, Expression arg2) {
        return new Subtraction(arg1, arg2);
    }

    @Override
	public boolean isNotRightAssociative() {
        return true;
    }

    @Override
	public int priority() {
        return 1;
    }
}

class DivisionFactory implements OperatorFactory {
    @Override
	public BinaryOperator build(Expression arg1, Expression arg2) {
        return new Division(arg1, arg2);
    }

    @Override
	public boolean isNotRightAssociative() {
        return true;
    }

    @Override
	public int priority() {
        return 3;
    }
}

class ProductFactory implements OperatorFactory {
    @Override
	public BinaryOperator build(Expression arg1, Expression arg2) {
        return new Product(arg1, arg2);
    }

    @Override
	public boolean isNotRightAssociative() {
        return false;
    }

    @Override
	public int priority() {
        return 3;
    }
}

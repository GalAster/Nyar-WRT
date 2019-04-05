package nyar;

public class Translator extends NyarBaseVisitor<String> {
    public String visitProgram(NyarParser.ProgramContext ctx) {
        StringBuilder result = new StringBuilder();
        for (int i = 1; i < ctx.getChildCount(); i++) {
            //System.out.print("Statement: " + ctx.statement(i-1).getText() + "\n");
            result.append(this.visit(ctx.statement(i - 1)));
        }
        return result.toString();
    }

    public String visitStatement(NyarParser.StatementContext ctx) {
        String result = "";
        if (ctx.block() != null) {
            result += this.visit(ctx.block());
        }
        if (ctx.emptyStatement() != null) {
            result += this.visit(ctx.emptyStatement());
        }
        if (ctx.expressionStatement() != null) {
            result += this.visit(ctx.expressionStatement());
        }
        if (ctx.assignStatement() != null) {
            result += this.visit(ctx.assignStatement());
        }
        if (ctx.ifStatement() != null) {
            result += this.visit(ctx.ifStatement());
        }
        return String.format("%s;", result);
    }

    public String visitBlock(NyarParser.BlockContext ctx) {
        return visitChildren(ctx);
    }

    public String visitEmptyStatement(NyarParser.EmptyStatementContext ctx) {
        //System.out.print("EmptyStatement!!\n");
        return "Null";
    }

    public String visitExpressionStatement(NyarParser.ExpressionStatementContext ctx) {
        StringBuilder result = new StringBuilder();
        for (int i = 1; i < ctx.getChildCount(); i++) {
            //System.out.print("Expression: " + ctx.expression(i-1).getText() + "\n");
            result.append(this.visit(ctx.expression(i - 1)));
        }
        return result.toString();
    }

    public String visitPriorityExpression(NyarParser.PriorityExpressionContext ctx) {
        String expression = this.visit(ctx.expression());
        //System.out.print("Priority: " + ctx.expression().getText() + "\n");
        return String.format("%s", expression);
    }

    public String visitPrefixExpression(NyarParser.PrefixExpressionContext ctx) {
        String expr = this.visit(ctx.expression());
        //System.out.printf("Operator: %s (%s);\n", ctx.op.getText(), expr);
        switch (ctx.op.getText()) {
            case "+":
                return String.format("Plus[%s]", expr);
            case "-":
                return String.format("Minus[%s]", expr);
            default:
                return String.format("UnknowOperator[%s]", expr);
        }
    }

    public String visitOperatorAssignExpression(NyarParser.OperatorAssignExpressionContext ctx) {
        String id = ctx.id.getText();
        String expr = this.visit(ctx.expression());
        //System.out.printf("Set[%s,%s];\n", id, expr);
        return String.format("Set[%s,%s]", id, expr);
    }


    public String visitPlus_Like(NyarParser.Plus_LikeContext ctx) {
        String lhs = this.visit(ctx.left);
        String rhs = this.visit(ctx.right);
        //System.out.printf("Operator: %s (%s,%s);\n", ctx.op.getText(), lhs, rhs);
        switch (ctx.op.getText()) {
            case "+":
                return String.format("Plus[%s,%s]", lhs, rhs);
            case "-":
                return String.format("Subtract[%s,%s]", lhs, rhs);
            default:
                return String.format("UnknowOperator[%s,%s]", lhs, rhs);
        }
    }

    public String visitMultiply_Like(NyarParser.Multiply_LikeContext ctx) {
        String lhs = this.visit(ctx.left);
        String rhs = this.visit(ctx.right);
        //System.out.printf("Operator: %s (%s,%s);\n", ctx.op.getText(), lhs, rhs);
        switch (ctx.op.getText()) {
            case "*":
                return String.format("Times[%s,%s]", lhs, rhs);
            case "/":
                return String.format("Divide[%s,%s]", lhs, rhs);
            default:
                return String.format("UnknowOperator[%s,%s]", lhs, rhs);
        }
    }

    public String visitPower_Like(NyarParser.Power_LikeContext ctx) {
        String lhs = this.visit(ctx.left);
        String rhs = this.visit(ctx.right);
        //System.out.printf("Operator: %s (%s,%s);\n", ctx.op.getText(), lhs, rhs);
        switch (ctx.op.getText()) {
            case "^":
                return String.format("Power[%s,%s]", lhs, rhs);
            case "√":
                return String.format("Surd[%s,%s]", rhs, lhs);
            default:
                return String.format("UnknowOperator[%s,%s]", lhs, rhs);
        }
    }

    public String visitNumber(NyarParser.NumberContext ctx) {
        //System.out.printf("%s\n", ctx.getText());
        switch (ctx.atom.getType()) {
            case NyarParser.Integer:
                return ctx.getText();
            case NyarParser.Float:
                return ctx.getText();
            default:
                return ctx.getText();
        }
    }

    public String visitSymbol(NyarParser.SymbolContext ctx) {
        //System.out.printf("%s\n", ctx.getText());
        return ctx.getText();
    }

    public String visitIfStatement(NyarParser.IfStatementContext ctx) {
        int else_count = ctx.elseifStatement().Else().size();
        int then_count = 0;
        if (ctx.Else() != null) {
            then_count += 1;
        }
        //System.out.printf("If Statement: %s\n", ctx.getText());
        //System.out.printf("ElseIf Count: %s\n", else_count + then_count);
        switch (else_count + then_count) {
            case 0: {
                String cond = this.visit(ctx.condition().expression());
                String then = this.visit(ctx.condition().expr_block());
                System.out.printf("Nyar`Core`If[%s,%s];\n", cond, then);
                return String.format("Nyar`Core`If[%s,%s]", cond, then);
            }
            case 1: {
                String cond = this.visit(ctx.condition().expression());
                String then = this.visit(ctx.condition().expr_block());
                String expr = this.visit(ctx.expr_block());
                System.out.printf("Nyar`Core`If[%s,%s,%s];\n", cond, then, expr);
                return String.format("Nyar`Core`If[%s,%s,%s]", cond, then, expr);
            }
            default: {
                String cond = this.visit(ctx.condition().expression());
                String then = this.visit(ctx.condition().expr_block());
                String expr = this.visit(ctx.expr_block());
                System.out.printf("Switch[%s,%s,___,%s];\n", cond, then, expr);
                return String.format("Switch[%s,%s,___,%s]", cond, then, expr);
            }
        }
    }
}
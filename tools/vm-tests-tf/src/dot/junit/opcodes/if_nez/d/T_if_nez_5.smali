.source "T_ifne_5.java"
.class  public Ldot/junit/opcodes/if_nez/d/T_if_nez_5;
.super  Ljava/lang/Object;


.method public constructor <init>()V
.registers 1

       invoke-direct {v0}, Ljava/lang/Object;-><init>()V
       return-void
.end method

.method public run(I)I
.registers 6

       if-nez v6, :Label9
       const/16 v6, 1234
       return v6

:Label9
       const/4 v6, 1
       return v6
.end method

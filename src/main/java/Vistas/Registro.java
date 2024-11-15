<?xml version="1.0" encoding="UTF-8"?>

<?import javafx.scene.control.Button?>
<?import javafx.scene.control.CheckBox?>
<?import javafx.scene.control.DatePicker?>
<?import javafx.scene.control.PasswordField?>
<?import javafx.scene.control.ScrollPane?>
<?import javafx.scene.control.TextField?>
<?import javafx.scene.image.Image?>
<?import javafx.scene.image.ImageView?>
<?import javafx.scene.layout.AnchorPane?>
<?import javafx.scene.layout.VBox?>
<?import javafx.scene.text.Font?>
<?import javafx.scene.text.Text?>

<AnchorPane maxHeight="-Infinity" maxWidth="-Infinity" minHeight="-Infinity" minWidth="-Infinity" prefHeight="798.0" prefWidth="451.0" style="-fx-background-color: #905D5D; -fx-background-radius: 20px; -fx-border-color: transparent; -fx-border-radius: 20px;" xmlns="http://javafx.com/javafx/22" xmlns:fx="http://javafx.com/fxml/1" fx:controller="Controladores.Registro">
    <children>
        <Text fill="WHITE" layoutX="155.0" layoutY="43.0" strokeType="OUTSIDE" strokeWidth="0.0" text="REGISTRO">
            <font>
                <Font name="Insaniburger" size="32.0" />
            </font>
        </Text>
        <Button fx:id="Cerrar" layoutX="374.0" layoutY="13.0" mnemonicParsing="false" onAction="#cerrar" prefHeight="49.0" prefWidth="60.0" style="-fx-background-color: transparent;">
            <graphic>
                <ImageView fitHeight="30.0" fitWidth="30.0" layoutX="391.0" layoutY="26.0" pickOnBounds="true" preserveRatio="true">
                    <image>
                        <Image url="@/eliminar.png" />
                    </image>
                </ImageView>
            </graphic>
        </Button>

        <!-- ScrollPane -->
        <ScrollPane hbarPolicy="NEVER" hmax="3.0" layoutX="-1.0" layoutY="60.0" prefHeight="759.0" prefWidth="450.0" style="-fx-background-color: #905D5D;" vvalue="1.0">
            <content>
                <VBox prefHeight="950" prefWidth="1140.0" spacing="10.0" style="-fx-background-color: #905D5D;">
                    <Text fill="WHITE" strokeType="OUTSIDE" strokeWidth="0.0" text="NOMBRE" textAlignment="CENTER" wrappingWidth="417.518177986145">
                        <font>
                            <Font name="Insaniburger" size="20.0" />
                        </font>
                    </Text>
                    <TextField fx:id="Nombre" prefHeight="25.0" prefWidth="404.0" />
                    <Text fill="WHITE" strokeType="OUTSIDE" strokeWidth="0.0" text="APELLIDOS" textAlignment="CENTER" wrappingWidth="418.87107849121094">
                        <font>
                            <Font name="Insaniburger" size="20.0" />
                        </font>
                    </Text>
                    <TextField fx:id="Apellidos" prefHeight="25.0" prefWidth="347.0" />
               <Text fill="WHITE" strokeType="OUTSIDE" strokeWidth="0.0" text="Nombre de usuario" textAlignment="CENTER" wrappingWidth="424.4048843383789">
                  <font>
                     <Font name="Insaniburger" size="20.0" />
                  </font>
               </Text>
               <TextField fx:id="Username" prefHeight="25.0" prefWidth="347.0" />
                    <Text fill="WHITE" strokeType="OUTSIDE" strokeWidth="0.0" text="EMAIL" textAlignment="CENTER" wrappingWidth="421.1672782897949">
                        <font>
                            <Font name="Insaniburger" size="20.0" />
                        </font>
                    </Text>
                    <TextField fx:id="Email" prefHeight="25.0" prefWidth="347.0" />
                    <Text fill="WHITE" strokeType="OUTSIDE" strokeWidth="0.0" text="CONTRASEÑA" textAlignment="CENTER" wrappingWidth="424.4048843383789">
                        <font>
                            <Font name="Insaniburger" size="20.0" />
                        </font>
                    </Text>
                    <PasswordField fx:id="Password" prefHeight="11.0" prefWidth="406.0" />
               <Text fill="WHITE" strokeType="OUTSIDE" strokeWidth="0.0" text="telefono" textAlignment="CENTER" wrappingWidth="424.4048843383789">
                  <font>
                     <Font name="Insaniburger" size="20.0" />
                  </font>
               </Text>
               <TextField fx:id="Telefono" />
               <Text fill="WHITE" strokeType="OUTSIDE" strokeWidth="0.0" text="direccion" textAlignment="CENTER" wrappingWidth="424.4048843383789">
                  <font>
                     <Font name="Insaniburger" size="20.0" />
                  </font>
               </Text>
               <TextField fx:id="Direccion" />
               <Text fill="WHITE" strokeType="OUTSIDE" strokeWidth="0.0" text="fecha de nacimiento" textAlignment="CENTER" wrappingWidth="424.4048843383789">
                  <font>
                     <Font name="Insaniburger" size="20.0" />
                  </font>
               </Text>
               <DatePicker fx:id="Fecha_Nacimiento" prefHeight="23.0" prefWidth="424.0" />
               <Text fill="WHITE" strokeType="OUTSIDE" strokeWidth="0.0" textAlignment="CENTER" wrappingWidth="420.681640625">
                  <font>
                     <Font name="Insaniburger" size="12.0" />
                  </font>
               </Text>
               <Text fill="WHITE" strokeType="OUTSIDE" strokeWidth="0.0" text="Al crear una cuenta, aceptas nuestros Términos y Condiciones Esto significa que consentirás el procesamiento de tus datos personales de acuerdo .con nuestras políticas" textAlignment="CENTER" wrappingWidth="420.681640625">
                  <font>
                     <Font name="Insaniburger" size="12.0" />
                  </font>
               </Text>
               <CheckBox fx:id="Terms" mnemonicParsing="false" prefHeight="17.0" prefWidth="392.0" text=" Acepto los terminos y condiciones" textAlignment="CENTER" />
               <Button fx:id="Registrarse" alignment="CENTER" mnemonicParsing="false" onAction="#registrarse" prefHeight="68.0" prefWidth="439.0" style="-fx-background-color: #F980A9;" text="Registrarse" textFill="WHITE">
                  <font>
                     <Font name="Insaniburger" size="17.0" />
                  </font>
               </Button>
                </VBox>
            </content>
        </ScrollPane>
    </children>
</AnchorPane>

package br.edu.ifsp.scl.calculadoradebitcoin

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.bloco_cotacao.*
import kotlinx.android.synthetic.main.bloco_entrada.*
import kotlinx.android.synthetic.main.bloco_saida.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONObject
import java.net.URL
import java.text.NumberFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    var cotacaoBitcoin = 0.0
    val API_URL = "https://www.mercadobitcoin.net/api/BTC/ticker/"
    val ID_REQUEST_READ_CONTACTS = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buscarCotacao()
        btn_calcular.setOnClickListener {
            calcular()

            // Trabalhando com permissões
            // Se permissão para ler contatos está concedida
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                // Permissão para ler contatos concedida
            } else {
                // Senão requisita a permissão
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), ID_REQUEST_READ_CONTACTS)
            }
        }
    }


    fun buscarCotacao(){
        doAsync {
            // Acessar a API do Mercado Bitcoin e guardar seu resultado
            val resposta = URL(API_URL).readText()

            // Acessar a propriedade que desejamos retornar da API
            cotacaoBitcoin =  JSONObject(resposta).getJSONObject("ticker").getDouble("last")

            // Formatação para Moeda Brasileira
            val f = NumberFormat.getCurrencyInstance(Locale("pt", "br"))
            val cotacaoFormatada = f.format(cotacaoBitcoin)


            uiThread {
                // Atualizando a tela com a cotação atual
                txt_cotacao.setText(cotacaoFormatada)
            }
        }
    }

    // Função para efetuar o cálculo de conversão do Bitcoin
    fun calcular(){
        // Veriffica se o usuário digitou algum dado
        if (txt_valor.text.isEmpty()){
            txt_valor.error = "Preencha um valor"
            return
        } else {
            // Captura os dados digitados e efetua o cálculo da cotação
            val valor_digitado = txt_valor.text.toString().replace(",", ".").toDouble()
            val resultado = if (cotacaoBitcoin > 0) valor_digitado/cotacaoBitcoin else 0.0
            txt_qtdeBitcoins.text = "%.8f".format(resultado)
            notificacaoSimples("Calculadora Bitcoin", "Cálculo efetuado com sucesso")
        }
    }


    // Função que retorna todas as requisições de permissão
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == ID_REQUEST_READ_CONTACTS) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // Permissão concedida e o aplicativo pode utilizar o recurso
            } else {
                //A permissão não foi concedida, desabilitar função que utiliza o recurso
            }
        }
    }
}

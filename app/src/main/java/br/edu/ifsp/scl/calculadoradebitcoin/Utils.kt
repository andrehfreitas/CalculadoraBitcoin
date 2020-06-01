package br.edu.ifsp.scl.calculadoradebitcoin

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat


// Função de extensão da classe Activity para criação de uma simples notificação do app
fun Activity.notificacaoSimples(title: String, message: String){
    // Criando o objeto de notificação
    val nBuilder = NotificationCompat.Builder(this, "default")
    // Definindo um ícone para a notificação
    nBuilder.setSmallIcon(R.mipmap.ic_launcher)

    // Definindo um título para a notificação
    nBuilder.setContentTitle(title)

    // Definindo um texto para a notificação
    nBuilder.setContentText(message)

    val notificacao = nBuilder.build()
    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    // Verificando a versão do sistema Android para criação ou não do canal de notificação
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel("default", "Canal de notificação Teste", NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)
    }

    // Enviando a notificação
    notificationManager.notify(1, notificacao)
}
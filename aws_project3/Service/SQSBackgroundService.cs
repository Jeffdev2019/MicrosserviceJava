using System;
using System.Collections;
using System.Threading;
using System.Threading.Tasks;
using Amazon.CDK;
using Amazon.SQS;
using Amazon.SQS.Model;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Logging;
using Environment = Amazon.CDK.Environment;

namespace MeuProjeto.Services
{
    public class SQSBackgroundService : BackgroundService
    {
        private readonly ILogger<SQSBackgroundService> _logger;
        private readonly IAmazonSQS _sqsClient;
        private readonly string _queueUrl = System.Environment.GetEnvironmentVariable("AWS_SQS_QUEUE_PRODUCT_EVENTS_URL");

        public SQSBackgroundService(ILogger<SQSBackgroundService> logger, IAmazonSQS sqsClient) : base()
        {
            _logger = logger;
            _sqsClient = sqsClient;

        }

        protected override async Task ExecuteAsync(CancellationToken stoppingToken)
        {

            _logger.LogInformation("Iniciando serviço de processamento de fila SQS...");

            while (!stoppingToken.IsCancellationRequested)
            {
                try
                {
                    var receiveMessageRequest = new ReceiveMessageRequest
                    {
                        QueueUrl = _queueUrl
                    };

                    var response = await _sqsClient.ReceiveMessageAsync(receiveMessageRequest, stoppingToken);

                    foreach (var message in response.Messages)
                    {
                        // Processar a mensagem aqui
                        _logger.LogInformation($"Mensagem da fila SQS recebida: {message.Body}");

                        // Exemplo: Deletar mensagem da fila após processamento
                        var deleteMessageRequest = new DeleteMessageRequest
                        {
                            QueueUrl = _queueUrl,
                            ReceiptHandle = message.ReceiptHandle
                        };
                        await _sqsClient.DeleteMessageAsync(deleteMessageRequest, stoppingToken);
                    }
                }
                catch (Exception ex)
                {
                    _logger.LogError($"Erro ao processar mensagem da fila SQS: {ex.Message}");
                }

                await Task.Delay(TimeSpan.FromSeconds(5), stoppingToken); // Esperar 5 segundos antes de verificar novamente
            }
        }
    }
}

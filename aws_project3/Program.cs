using Amazon.SQS;
using MeuProjeto.Services;
using Amazon.Extensions.NETCore.Setup;
using Microsoft.Extensions.Configuration;
using System.Configuration;
using Microsoft.AspNetCore.Diagnostics.HealthChecks;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.

builder.Services.AddControllers();
// Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();
builder.Services.AddDefaultAWSOptions(new AWSOptions
 {
     Region = Amazon.RegionEndpoint.USEast1 
 });
builder.Services.AddAWSService<IAmazonSQS>();
builder.Services.AddHostedService<SQSBackgroundService>();
builder.Services.AddHealthChecks();

var app = builder.Build();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseAuthorization();

app.MapControllers();
app.UseRouting();

app.UseEndpoints(endpoints =>
{
    endpoints.MapHealthChecks("/actuator/health", new HealthCheckOptions
    {
        ResponseWriter = async (context, report) =>
        {
            var result = System.Text.Json.JsonSerializer.Serialize(new
            {
                status = report.Status.ToString()
            });

            context.Response.ContentType = "application/json";
            await context.Response.WriteAsync(result);
        }
    });
});

app.Run();

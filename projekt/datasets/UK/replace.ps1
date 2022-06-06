$files = Get-ChildItem ".\*.csv"

foreach ($f in $files)
{
    $outfile = $f.FullName 
    (Get-Content $f.FullName) -replace "'", " " | Set-Content $outfile
}